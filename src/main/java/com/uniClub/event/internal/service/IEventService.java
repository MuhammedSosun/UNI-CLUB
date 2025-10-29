package com.uniClub.event.internal.service;

import com.uniClub.event.api.dto.EventRequest;
import com.uniClub.event.api.dto.EventResponse;
import com.uniClub.event.internal.entity.Event;

import java.util.List;

public interface IEventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> findAllEvents();
    List<EventResponse> searchEvents(String filter);
    EventResponse findEventById(Long id);
    EventResponse updateEvent(EventRequest eventRequest, Long id);
    EventResponse joinEvent(Long eventId);
    EventResponse leaveEvent(Long eventId);
    String deleteEvent(Long id);
}
