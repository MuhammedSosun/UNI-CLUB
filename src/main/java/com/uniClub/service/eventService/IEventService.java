package com.uniClub.service.eventService;

import com.uniClub.dto.eventDto.EventRequest;
import com.uniClub.dto.eventDto.EventResponse;

import java.util.List;

public interface IEventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> findAllEvents();
    EventResponse findEventById(Long id);
    EventResponse updateEvent(EventRequest eventRequest, Long id);
    String deleteEvent(Long id);
}
