package com.uniClub.controller.eventController;

import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.eventDto.EventRequest;
import com.uniClub.dto.eventDto.EventResponse;


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
