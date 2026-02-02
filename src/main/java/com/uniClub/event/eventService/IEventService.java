package com.uniClub.event.eventService;

import com.uniClub.Club.clubDto.ActiveClubDTO;
import com.uniClub.event.eventDto.EventParticipantDto;
import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.event.eventDto.ParticipationStatusRequest;
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
    EventResponse joinEvent(Long eventId);
    void changeParticipationStatus(Long eventId, ParticipationStatusRequest request);
    Page<EventParticipantDto> getParticipantsPaged(Long eventId, String filter, Pageable pageable);

}
