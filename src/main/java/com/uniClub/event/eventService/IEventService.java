package com.uniClub.service.eventService;

import com.uniClub.dto.clubDto.ActiveClubDTO;
import com.uniClub.dto.eventDto.EventRequest;
import com.uniClub.dto.eventDto.EventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> findAllEvents();
    List<EventResponse> searchEvents(String filter);
    EventResponse findEventById(Long id);
    EventResponse updateEvent(EventRequest eventRequest, Long id);
    //EventResponse joinEvent(Long eventId);
    //EventResponse leaveEvent(Long eventId);
    String deleteEvent(Long id);
    Long totalEvents();
    Page<EventResponse> getUpcomingEventsPaged(Pageable pageable);
    List<ActiveClubDTO> getTopActiveClubsLast3Months();
    List<EventResponse> getTopEventsThisMonth();

}
