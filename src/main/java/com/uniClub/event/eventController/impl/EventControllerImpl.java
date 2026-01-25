package com.uniClub.event.eventController.impl;

import com.uniClub.util.controller.RestBaseController;
import com.uniClub.util.controller.RootEntity;
import com.uniClub.Club.clubDto.ActiveClubDTO;
import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.event.eventController.IEventController;
import com.uniClub.event.eventService.IEventService;
import com.uniClub.util.pageable.PageUtil;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventControllerImpl extends RestBaseController implements IEventController {
    private final IEventService eventService;

    public EventControllerImpl(IEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    @Override
    public RootEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        return ok(eventService.createEvent(eventRequest));
    }

    @GetMapping("/list")
    @Override
    public RootEntity<List<EventResponse>> findAllEvents() {
        return ok(eventService.findAllEvents());
    }
    @GetMapping("/get/{id}")
    @Override
    public RootEntity<EventResponse> findEventById(@PathVariable Long id) {
        return ok(eventService.findEventById(id));
    }
    @GetMapping("/filter")
    @Override
    public RootEntity<List<EventResponse>> searchEvents(@RequestParam String filter) {
        return ok(eventService.searchEvents(filter));
    }
    /*
    @PostMapping("/{id}/join")
    public RootEntity<EventResponse> joinEvent(@PathVariable Long id) {
        return ok(eventService.joinEvent(id));
    }
////////////////////////////////////////////
    @PostMapping("/{id}/leave")
    public RootEntity<EventResponse> leaveEvent(@PathVariable Long id) {
        return ok(eventService.leaveEvent(id));
    }
*/
    @PutMapping("/update/{id}")
    @Override
    public RootEntity<EventResponse> updateEvent(@RequestBody EventRequest eventRequest,@PathVariable Long id) {
        return ok(eventService.updateEvent(eventRequest, id));
    }
    @DeleteMapping("/delete/{id}")
    @Override
    public RootEntity<String> deleteEvent(@PathVariable Long id) {
        ok(eventService.deleteEvent(id));
        return ok("Event deleted successfully");
    }
    @GetMapping("/total")
    @Override
    public RootEntity<Long> totalEvents() {
        return ok(eventService.totalEvents());
    }
    @PostMapping("/upcoming/paged")
    @Override
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

    @Override
    @GetMapping("/top/month")
    public RootEntity<List<EventResponse>> getTopEventsThisMonth() {
        return ok(eventService.getTopEventsThisMonth());
    }


}
