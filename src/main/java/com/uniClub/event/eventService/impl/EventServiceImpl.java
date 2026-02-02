package com.uniClub.event.eventService.impl;

import com.uniClub.Club.clubDto.ActiveClubDTO;
import com.uniClub.Club.clubEntity.ClubEntity;
// 👇 Yeni importlar
import com.uniClub.enums.ClubRole;
import com.uniClub.event.eventDto.EventParticipantDto;
import com.uniClub.event.eventDto.ParticipationStatusRequest;
import com.uniClub.event.eventEntity.EventParticipation;
import com.uniClub.event.eventRepository.EventParticipationRepository;
import com.uniClub.member.memberEntity.ClubMembership;
import com.uniClub.member.memberEntity.Member;
import com.uniClub.member.memberRepository.ClubMemberShipRepository;
import com.uniClub.member.memberRepository.MemberRepository;       // Bu repository lazım
// 👆
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.logging.LoggableOperation;
import com.uniClub.enums.OperationType;
import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.event.eventEntity.Event;
import com.uniClub.event.eventMapper.EventMapper;
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
import java.util.ArrayList;
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
    private final MemberRepository memberRepository;
    private final ClubMemberShipRepository clubMembershipRepository;
    private final EventParticipationRepository eventParticipationRepository;

    public EventServiceImpl(EventMapper eventMapper,
                            EventRepository eventRepository,
                            UserRepository userRepository,
                            ClubRepository clubRepository,
                            MemberRepository memberRepository,
                            ClubMemberShipRepository clubMembershipRepository, EventParticipationRepository eventParticipationRepository) {
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.memberRepository = memberRepository;
        this.clubMembershipRepository = clubMembershipRepository;
        this.eventParticipationRepository = eventParticipationRepository;
    }
    @Transactional
    @LoggableOperation(OperationType.CREATE_EVENT)
    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        String username = getUsername();

        validateEventRequest(eventRequest);


        if (eventRequest.getClubIds() != null && !eventRequest.getClubIds().isEmpty()) {
            checkClubAuthorization(username, new ArrayList<>(eventRequest.getClubIds()));
        }

        Event event = eventMapper.toEventEntity(eventRequest);
        event.setCreatedBy(username);
        event.setUpdatedBy(username);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

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


    @LoggableOperation(OperationType.UPDATE_EVENT)
    @Override
    public EventResponse updateEvent(EventRequest eventRequest, Long id) {

        return createEventLogicForUpdate(eventRequest, id);
    }


    private void checkClubAuthorization(String username, List<Long> clubIds) {
        Member member = memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.MEMBER_NOT_FOUND, username)));

        for (Long clubId : clubIds) {
            ClubMembership membership = clubMembershipRepository.findByClubIdAndMemberId(clubId, member.getId())
                    .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NOT_PARTICIPANT,
                            "User is not a member of club with id: " + clubId)));

            if (membership.getRole() != ClubRole.PRESIDENT && membership.getRole() != ClubRole.VICE_PRESIDENT) {
                log.warn("[EVENT_CREATE_DENIED] User '{}' tried to create event for Club '{}' but has role '{}'",
                        username, clubId, membership.getRole());

                throw new BaseException(new ErrorMessage(MessageType.UNAUTHORIZED_ACCESS,
                        "Only President or Vice President can create events for this club."));
            }
        }
    }


    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.EVENT_NOT_FOUND,id.toString())));
    }

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

    private EventResponse createEventLogicForUpdate(EventRequest eventRequest, Long id) {
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
    @Override
    public String deleteEvent(Long id) {
        // Sizin mevcut kodunuz...
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

    @Transactional
    @LoggableOperation(OperationType.JOIN_EVENT)
    @Override
    public EventResponse joinEvent(Long eventId) {
        String username = getUsername();
        Member member = memberRepository.findByUserUsername(username).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.MEMBER_NOT_FOUND, username))
        );

        Event event = getEventById(eventId);


        boolean alreadyJoined = eventParticipationRepository.existsByEventAndMember(event, member);
        if (alreadyJoined) {
            throw new BaseException(new ErrorMessage(MessageType.ALREADY_JOINED, "Bu etkinliğe zaten kayıtlısınız."));
        }



        boolean isAuthorized = false;

        if (event.getClubs() == null || event.getClubs().isEmpty()) {
            isAuthorized = true;
        } else {
            for (ClubEntity club : event.getClubs()) {

                boolean isMember = clubMembershipRepository.findByClubIdAndMemberId(club.getId(), member.getId())
                        .map(membership -> membership.getStatus().name().equals("APPROVED") || membership.getStatus().name().equals("ACTIVE"))
                        .orElse(false);

                if (isMember) {
                    isAuthorized = true;
                    break;
                }
            }
        }

        if (!isAuthorized) {
            throw new BaseException(new ErrorMessage(MessageType.UNAUTHORIZED_ACCESS,
                    "Bu etkinliğe katılmak için düzenleyen kulübün onaylı üyesi olmalısınız."));
        }

        EventParticipation participation = new EventParticipation();
        participation.setEvent(event);
        participation.setMember(member);
        participation.setJoinedAt(LocalDateTime.now());
        participation.setAttended(false);

        // 🔥 ARTIK DİREKT ONAYLANMIYOR, BEKLEMEDE KALIYOR
        participation.setStatus(com.uniClub.enums.ParticipationStatus.PENDING);

        // BaseEntity alanları
        participation.setCreatedBy(username);
        participation.setUpdatedBy(username);
        participation.setCreatedAt(LocalDateTime.now());
        participation.setUpdatedAt(LocalDateTime.now());

        eventParticipationRepository.save(participation);

        // ⚠️ DİKKAT: Henüz onaylanmadığı için katılımcı sayısını artırmıyoruz!
        // Onaylama işlemi (approveEventParticipation) yapıldığında artırılacak.
        // event.setParticipantCount(...) -> BU SATIRI SİLİYORUZ veya YORUMA ALIYORUZ.

        log.info("[EVENT_JOIN_REQUEST] user='{}' event='{}'", username, event.getTitle());

        return eventMapper.toEventResponse(event);
    }
    @Transactional
    @Override
    public void changeParticipationStatus(Long eventId, ParticipationStatusRequest request) {
        String currentUsername = getUsername();
        Event event = getEventById(eventId);

        // 1. YETKİ KONTROLÜ: Sadece etkinliği oluşturan kişi (veya Admin) onaylayabilir
        // (İstersen buraya kulüp başkanlarını da dahil edebilirsin checkClubAuthorization ile)
        if (!event.getCreatedBy().equals(currentUsername)) {
            // Basitlik olsun diye sadece oluşturan baksın dedik, istersen admin rolü de eklenebilir
            throw new BaseException(new ErrorMessage(MessageType.UNAUTHORIZED_ACCESS,
                    "Bu başvuruyu sadece etkinliği oluşturan kişi yönetebilir."));
        }

        // 2. Başvuru Kaydını Bul
        EventParticipation participation = eventParticipationRepository.findByEventIdAndMemberId(eventId, request.getMemberId())
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Başvuru bulunamadı.")));

        // Eski durumu sakla (Sayıyı artırıp azaltacağımıza karar vermek için)
        var oldStatus = participation.getStatus();
        var newStatus = request.getStatus();

        // 3. Durumu Güncelle
        participation.setStatus(newStatus);
        participation.setUpdatedBy(currentUsername);
        participation.setUpdatedAt(LocalDateTime.now());

        // Eğer onaylandıysa "attended" (katıldı) değil ama listeye girdi kabul edilir.
        // attended alanı etkinlik bittikten sonra "gerçekten geldi mi?" için kullanılabilir.

        eventParticipationRepository.save(participation);

        // 4. KATILIMCI SAYISINI GÜNCELLEME MANTIĞI
        boolean countChanged = false;

        // PENDING veya REJECTED -> APPROVED olursa sayı ARTAR
        if (oldStatus != com.uniClub.enums.ParticipationStatus.APPROVED &&
                newStatus == com.uniClub.enums.ParticipationStatus.APPROVED) {

            event.setParticipantCount(event.getParticipantCount() + 1);
            countChanged = true;
        }

        // APPROVED -> REJECTED veya PENDING olursa sayı AZALIR (Onaylı birini reddetmek)
        else if (oldStatus == com.uniClub.enums.ParticipationStatus.APPROVED &&
                newStatus != com.uniClub.enums.ParticipationStatus.APPROVED) {

            event.setParticipantCount(event.getParticipantCount() > 0 ? event.getParticipantCount() - 1 : 0);
            countChanged = true;
        }

        if (countChanged) {
            eventRepository.save(event);
        }

        log.info("[PARTICIPATION_STATUS_CHANGE] event={} member={} old={} new={}",
                eventId, request.getMemberId(), oldStatus, newStatus);
    }
    @Override
        public Page<EventParticipantDto> getParticipantsPaged(Long eventId, String filter, Pageable pageable) {
        // Repository'den sayfalı veriyi çek
        Page<EventParticipation> page = eventParticipationRepository.findParticipantsByEventId(eventId, filter, pageable);

        // DTO'ya çevir (MapStruct ile)
        return page.map(eventMapper::toParticipantDto);
    }
}