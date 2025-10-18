package com.uniClub.event.internal.service;

import com.uniClub.event.api.dto.EventRequest;
import com.uniClub.event.api.dto.EventResponse;
import com.uniClub.event.internal.entity.Event;

import java.util.List;

public interface IEventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> findAllEvents();
    EventResponse findEventById(Long id);
    EventResponse updateEvent(EventRequest eventRequest, Long id);
    String deleteEvent(Long id);
}
