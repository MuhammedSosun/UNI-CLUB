package com.uniClub.event.eventController;

import com.uniClub.util.controller.RootEntity;
import com.uniClub.Club.clubDto.ActiveClubDTO;
import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;


import java.util.List;

public interface IEventController {
    RootEntity<EventResponse> createEvent(EventRequest eventRequest);
    RootEntity<List<EventResponse>> findAllEvents();
    RootEntity<EventResponse> findEventById(Long id);
    RootEntity<List<EventResponse>> searchEvents(String filter);
    //RootEntity<EventResponse> joinEvent(Long id);
    //RootEntity<EventResponse> leaveEvent(Long id);
    RootEntity<EventResponse> updateEvent(EventRequest eventRequest, Long id);
    RootEntity<String> deleteEvent(Long id);
    RootEntity<Long> totalEvents();
    RootEntity<PageableEntity<EventResponse>> getUpcomingEventsPaged(PageableRequest request);
    RootEntity<List<ActiveClubDTO>> getTopActiveClubsLast3Months();
    RootEntity<List<EventResponse>> getTopEventsThisMonth();
}
