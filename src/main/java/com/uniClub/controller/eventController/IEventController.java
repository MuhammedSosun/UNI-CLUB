package com.uniClub.controller.eventController;

import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.eventDto.EventRequest;
import com.uniClub.dto.eventDto.EventResponse;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

public interface IEventController {
    RootEntity<EventResponse> createEvent(EventRequest eventRequest);
    RootEntity<List<EventResponse>> findAllEvents();
    RootEntity<EventResponse> findEventById(Long id);
    RootEntity<EventResponse> updateEvent(EventRequest eventRequest, Long id);
    RootEntity<String> deleteEvent(@PathVariable Long id);
}
