package com.uniClub.event.eventController;

import com.uniClub.event.eventDto.EventParticipantDto;
import com.uniClub.event.eventDto.ParticipationStatusRequest;
import com.uniClub.util.controller.RootEntity;
import com.uniClub.Club.clubDto.ActiveClubDTO;
import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

public interface IEventController {
    RootEntity<EventResponse> createEvent(EventRequest eventRequest);
    RootEntity<List<EventResponse>> findAllEvents();
    RootEntity<EventResponse> findEventById(Long id);
    RootEntity<List<EventResponse>> searchEvents(String filter);
    RootEntity<EventResponse> joinEvent(Long id);
    RootEntity<String> changeParticipationStatus(Long eventId, ParticipationStatusRequest request);
    RootEntity<Page<EventParticipantDto>> getParticipantsPaged(Long id,String filter, Pageable pageable);
    //RootEntity<EventResponse> leaveEvent(Long id);
    RootEntity<EventResponse> updateEvent(EventRequest eventRequest, Long id);
    RootEntity<String> deleteEvent(Long id);
    RootEntity<Long> totalEvents();
    RootEntity<PageableEntity<EventResponse>> getUpcomingEventsPaged(PageableRequest request);
    RootEntity<List<ActiveClubDTO>> getTopActiveClubsLast3Months();
    RootEntity<List<EventResponse>> getTopEventsThisMonth();
}
