package com.uniClub.event.internal.controller.impl;

import com.uniClub.common.controller.RootEntity;
import com.uniClub.event.api.dto.EventRequest;
import com.uniClub.event.api.dto.EventResponse;
import com.uniClub.event.internal.controller.IEventController;
import com.uniClub.event.internal.service.IEventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.uniClub.common.controller.RootEntity.ok;

@RestController
@RequestMapping("/rest/api/event")
public class EventControllerImpl implements IEventController {
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
}
