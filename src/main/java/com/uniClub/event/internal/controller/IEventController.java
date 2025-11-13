package com.uniClub.event.internal.controller;

import com.uniClub.common.controller.RootEntity;
import com.uniClub.event.api.dto.EventRequest;
import com.uniClub.event.api.dto.EventResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

public interface IEventController {
    RootEntity<EventResponse> createEvent(EventRequest eventRequest);
    RootEntity<List<EventResponse>> findAllEvents();
    RootEntity<EventResponse> findEventById(Long id);
    RootEntity<List<EventResponse>> searchEvents(String filter);
    RootEntity<EventResponse> joinEvent(Long id);
    RootEntity<EventResponse> leaveEvent(Long id);
    RootEntity<EventResponse> updateEvent(EventRequest eventRequest, Long id);
    RootEntity<String> deleteEvent(Long id);
}
