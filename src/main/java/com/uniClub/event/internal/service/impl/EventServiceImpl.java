package com.uniClub.event.internal.service.impl;

import com.uniClub.common.exceptions.exception.BaseException;
import com.uniClub.common.exceptions.exception.ErrorMessage;
import com.uniClub.common.exceptions.exception.MessageType;
import com.uniClub.common.logging.LoggableOperation;
import com.uniClub.common.utils.OperationType;
import com.uniClub.event.api.dto.EventRequest;
import com.uniClub.event.api.dto.EventResponse;
import com.uniClub.event.internal.entity.Event;
import com.uniClub.event.internal.mapper.EventMapper;
import com.uniClub.event.internal.repository.EventRepository;
import com.uniClub.event.internal.service.IEventService;
import com.uniClub.user.api.UserPublicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class EventServiceImpl implements IEventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserPublicService userPublicService;

    public EventServiceImpl(EventMapper eventMapper, EventRepository eventRepository, UserPublicService userPublicService) {
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
        this.userPublicService = userPublicService;
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
        try {
            eventRepository.save(event);
            log.info("[EVENT_CREATED] user='{}' title='{}'", username, event.getTitle());
        }catch (Exception e) {
            log.error("[EVENT_CREATE_ERROR] user='{}' event='{}' msg='{}'",username, event.getTitle(), e.getMessage());
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
    @LoggableOperation(OperationType.JOIN_EVENT)
    @Override
    public EventResponse joinEvent(Long eventId) {
        String  username = getUsername();
        UUID userId = userPublicService.getUserIdByUsername(username);
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
    @LoggableOperation(OperationType.LEAVE_EVENT)
    @Override
    public EventResponse leaveEvent(Long eventId) {
        String username = getUsername();
        UUID userId = userPublicService.getUserIdByUsername(username);
        Event event = getEventById(eventId);
        if (!event.getParticipantIds().remove(userId)) {
            throw new BaseException(new ErrorMessage(MessageType.NOT_PARTICIPANT, "User not registered in this event"));
        }
        event.setParticipantCount(event.getParticipantCount() - 1);
        eventRepository.save(event);
        log.info("[EVENT_LEFT] user='{}' event='{}'", username, event.getTitle());
        return eventMapper.toEventResponse(event);
    }

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
