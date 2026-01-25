package com.uniClub.event.eventService.impl;

import com.uniClub.Club.clubDto.ActiveClubDTO;
import com.uniClub.Club.clubEntity.ClubEntity;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.logging.LoggableOperation;
import com.uniClub.enums.OperationType;
import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.event.eventEntity.Event;
import com.uniClub.mapper.eventMapper.EventMapper;
import com.uniClub.Club.clubRepository.ClubRepository;
import com.uniClub.event.eventRepository.EventRepository;
import com.uniClub.user.userRepository.UserRepository;
import com.uniClub.event.eventService.IEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements IEventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public EventServiceImpl(EventMapper eventMapper, EventRepository eventRepository, UserRepository userRepository, ClubRepository clubRepository) {
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }
    @LoggableOperation(OperationType.CREATE_EVENT)
    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        String username = getUsername();

        validateEventRequest(eventRequest);

        Event event = eventMapper.toEventEntity(eventRequest);
        event.setCreatedBy(username);
        event.setUpdatedBy(username);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());



        // 🔥 clubIds -> clubs eşlemesi
        if (eventRequest.getClubIds() != null && !eventRequest.getClubIds().isEmpty()) {
            Set<ClubEntity> clubs = eventRequest.getClubIds().stream()
                    .map(id -> clubRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Club not found: " + id)))
                    .collect(Collectors.toSet());
            event.setClubs(clubs);
        }

        try {
            eventRepository.save(event);
            log.info("[EVENT_CREATED] user='{}' title='{}'", username, event.getTitle());
        } catch (Exception e) {
            log.error("[EVENT_CREATE_ERROR] user='{}' event='{}' msg='{}'",
                    username, event.getTitle(), e.getMessage());
            throw new BaseException(new ErrorMessage(MessageType.EVENT_SAVE_DATABASE_ERROR, e.getMessage()));
        }

        return eventMapper.toEventResponse(event);
    }

    @LoggableOperation(OperationType.FIND_ALL_EVENTS)
    @Override
    public List<EventResponse> findAllEvents() {
        List<Event> events = eventRepository.findAllByOrderByEventDateAsc();
        if (events.isEmpty()) {
            log.warn("[EVENT_LIST_EMPTY]");
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,"No events found"));
        }
        log.info("[EVENT_LISTED] total={}", events.size());
        return events.stream().map(eventMapper::toEventResponse).toList();
    }

    public List<EventResponse> searchEvents(String filter) {
        List<Event> events = eventRepository.findByTitleContainingIgnoreCaseOrderByEventDateAsc(filter);
        return events.stream().map(eventMapper::toEventResponse).toList();
    }

    @LoggableOperation(OperationType.FIND_EVENT)
    @Override
    public EventResponse findEventById(Long id) {
        Event event = getEventById(id);
        log.debug("[EVENT_FOUND] id={} title={}", id, event.getTitle());
        return eventMapper.toEventResponse(event);
    }
    @LoggableOperation(OperationType.UPDATE_EVENT)
    @Override
    public EventResponse updateEvent(EventRequest eventRequest, Long id) {
        Event existingEvent = getEventById(id);
        String username = getUsername();

        if (!existingEvent.getCreatedBy().equals(username)) {
            log.warn("[EVENT_UPDATE_DENIED] id={} user='{}'", id, username);
            throw new BaseException(new ErrorMessage(MessageType.EVENT_USER_NOT_OWNER,
                    "User " + username + " is not the creator of event " + id));
        }

        if (eventRequest.getEventDate() != null && eventRequest.getEventDate().isBefore(LocalDateTime.now())) {
            log.warn("[EVENT_UPDATE_INVALID_DATE] id={} user='{}'", id, username);
            throw new BaseException(new ErrorMessage(MessageType.EVENT_DATE_INVALID,
                    "Event date cannot be in the past"));
        }
        existingEvent.setTitle(eventRequest.getTitle());
        existingEvent.setDescription(eventRequest.getDescription());
        existingEvent.setLocation(eventRequest.getLocation());
        existingEvent.setEventDate(eventRequest.getEventDate());
        existingEvent.setUpdatedBy(username);
        existingEvent.setUpdatedAt(LocalDateTime.now());

        eventRepository.save(existingEvent);
        log.info("[EVENT_UPDATED] id={} user='{}'", id, username);
        return eventMapper.toEventResponse(existingEvent);
    }
    /*
    @LoggableOperation(OperationType.JOIN_EVENT)
    @Override
    public EventResponse joinEvent(Long eventId) {
        String  username = getUsername();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.USERNAME_NOT_FOUND,username))
        );
        UUID userId = user.getId();
        Event event = getEventById(eventId);

        if (event.getParticipantIds().contains(userId)) {
            throw new BaseException(new ErrorMessage(MessageType.ALREADY_JOINED, "User already joined this event"));
        }
        event.getParticipantIds().add(userId);
        event.setParticipantCount(event.getParticipantCount() + 1);
        eventRepository.save(event);
        log.info("[EVENT_JOINED] user='{}' event='{}'", username, event.getTitle());
        EventResponse eventResponse = eventMapper.toEventResponse(event);

        return eventResponse;
    }
     //////////////////////////////////////////////////////////////////////

    @LoggableOperation(OperationType.LEAVE_EVENT)
    @Override
    public EventResponse leaveEvent(Long eventId) {
        String username = getUsername();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.USERNAME_NOT_FOUND,username))
        );
        UUID userId = user.getId();
        Event event = getEventById(eventId);
        if (!event.getParticipantIds().remove(userId)) {
            throw new BaseException(new ErrorMessage(MessageType.NOT_PARTICIPANT, "User not registered in this event"));
        }
        event.setParticipantCount(event.getParticipantCount() - 1);
        eventRepository.save(event);
        log.info("[EVENT_LEFT] user='{}' event='{}'", username, event.getTitle());
        return eventMapper.toEventResponse(event);
    }
*/
    @LoggableOperation(OperationType.DELETE_EVENT)
    @Override
    public String deleteEvent(Long id) {
        Event event = getEventById(id);
        String username = getUsername();

        if (!event.getCreatedBy().equals(username)) {
            log.warn("[EVENT_DELETE_DENIED] id={} user='{}'", id, username);
            throw new BaseException(new ErrorMessage(MessageType.EVENT_USER_NOT_OWNER,
                    "User " + username + " is not allowed to delete this event"));
        }

        try {
            eventRepository.delete(event);
            log.info("[EVENT_DELETED] id={} user='{}'", id, username);
            return "Event deleted successfully";
        } catch (Exception e) {
            log.error("[EVENT_DELETE_ERROR] id={} user='{}' msg='{}'", id, username, e.getMessage());
            throw new BaseException(new ErrorMessage(MessageType.EVENT_DELETE_FAILED, e.getMessage()));
        }

    }

    @Override
    public Long totalEvents() {
        return eventRepository.count();
    }
    @Transactional
    @LoggableOperation(OperationType.GET_UPCOMING_EVENTS_PAGED)
    @Override
    public Page<EventResponse> getUpcomingEventsPaged(Pageable pageable) {

        LocalDateTime now = LocalDateTime.now();
        Page<Event> events = eventRepository.findUpcomingEventsPaged(now, pageable);
        return events.map(eventMapper::toEventResponse);
    }

    @Override
    public List<ActiveClubDTO> getTopActiveClubsLast3Months() {

        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        List<Object[]> rows = eventRepository.findTopActiveClubsLast3Months(threeMonthsAgo);

        return rows.stream()
                .map(r -> {
                    ClubEntity club = (ClubEntity) r[0];
                    Long eventCount = (Long) r[1];
                    return new ActiveClubDTO(club.getClubName(), eventCount);
                })
                .limit(5)
                .toList();
    }

    @Override
    public List<EventResponse> getTopEventsThisMonth() {

        LocalDateTime start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1);

        List<Event> events = eventRepository.findTopEventsThisMonth(start, end);

        return events.stream()
                .limit(5)
                .map(eventMapper::toEventResponse)
                .collect(Collectors.toList());
    }



    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.EVENT_NOT_FOUND,id.toString())));
    }

    //validation
    private void validateEventRequest(EventRequest eventRequest) {
        if (eventRequest.getTitle() == null || eventRequest.getTitle().isBlank()) {
            throw new BaseException(new ErrorMessage(MessageType.VALIDATION_ERROR,"Title required"));
        }
        if (eventRequest.getEventDate() == null){
            throw new BaseException(new ErrorMessage(MessageType.VALIDATION_ERROR,"EventDate is required"));
        }
        if (eventRequest.getEventDate().isBefore(LocalDateTime.now())){
            throw new BaseException(new ErrorMessage(MessageType.EVENT_DATE_INVALID,
                    "Event date cannot be before current time"));
        }
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
