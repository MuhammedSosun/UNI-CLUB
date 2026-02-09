package com.uniClub.event.eventController;

import com.uniClub.event.eventDto.EventParticipantDto;
import com.uniClub.event.eventDto.ParticipationStatusRequest;
import com.uniClub.util.controller.RestBaseController;
import com.uniClub.util.controller.RootEntity;
import com.uniClub.Club.clubDto.ActiveClubDTO;
import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.event.eventService.IEventService;
import com.uniClub.util.pageable.PageUtil;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventControllerImpl extends RestBaseController{
    private final IEventService eventService;

    public EventControllerImpl(IEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    
    public RootEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        return ok(eventService.createEvent(eventRequest));
    }

    @GetMapping("/list")
    
    public RootEntity<List<EventResponse>> findAllEvents() {
        return ok(eventService.findAllEvents());
    }

    @GetMapping("/get/{id}")
    
    public RootEntity<EventResponse> findEventById(@PathVariable Long id) {
        return ok(eventService.findEventById(id));
    }

    @GetMapping("/filter")
    
    public RootEntity<List<EventResponse>> searchEvents(@RequestParam String filter) {
        return ok(eventService.searchEvents(filter));
    }


    @PostMapping("/{id}/join")
    
    public RootEntity<EventResponse> joinEvent(@PathVariable Long id) {
        return ok(eventService.joinEvent(id));
    }
    @PostMapping("/{eventId}/participation-status")
    
    public RootEntity<String> changeParticipationStatus(
            @PathVariable Long eventId,
            @RequestBody ParticipationStatusRequest request) {

        eventService.changeParticipationStatus(eventId, request);
        return ok("Başvuru durumu başarıyla güncellendi: " + request.getStatus());
    }
    @GetMapping("/{id}/participants")
    public RootEntity<Page<EventParticipantDto>> getParticipantsPaged(
            @PathVariable Long id,
            @RequestParam(required = false) String filter,
            @PageableDefault(size = 10) Pageable pageable // Varsayılan 10 kişi getir
    ) {
        return ok(eventService.getParticipantsPaged(id, filter, pageable));
    }

    // (leaveEvent metodunu servis tarafında güncellemediğimiz için şimdilik kapalı kalabilir
    // veya aynı mantıkla EventParticipationRepository üzerinden silme işlemi yaparak açabilirsin)
    /*
    @PostMapping("/{id}/leave")
    public RootEntity<EventResponse> leaveEvent(@PathVariable Long id) {
        return ok(eventService.leaveEvent(id));
    }
    */

    @PutMapping("/update/{id}")
    
    public RootEntity<EventResponse> updateEvent(@RequestBody EventRequest eventRequest, @PathVariable Long id) {
        return ok(eventService.updateEvent(eventRequest, id));
    }

    @DeleteMapping("/delete/{id}")
    
    public RootEntity<String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id); // Buradaki ok() silindi, void döndüğü için direkt çağırıyoruz
        return ok("Event deleted successfully");
    }

    @GetMapping("/total")
    
    public RootEntity<Long> totalEvents() {
        return ok(eventService.totalEvents());
    }

    @PostMapping("/upcoming/paged")
    
    public RootEntity<PageableEntity<EventResponse>> getUpcomingEventsPaged(
            @RequestBody PageableRequest request
    ) {
        request.setColumnName("eventDate");
        request.setAsc(true);
        Pageable pageable = PageUtil.toPageable(request);
        Page<EventResponse> page = eventService.getUpcomingEventsPaged(pageable);
        return ok(PageUtil.toPageableResponse(page, page.getContent()));
    }

    @GetMapping("/stats/top-active-clubs")
    public RootEntity<List<ActiveClubDTO>> getTopActiveClubsLast3Months() {
        return ok(eventService.getTopActiveClubsLast3Months());
    }

    
    @GetMapping("/top/month")
    public RootEntity<List<EventResponse>> getTopEventsThisMonth() {
        return ok(eventService.getTopEventsThisMonth());
    }
}