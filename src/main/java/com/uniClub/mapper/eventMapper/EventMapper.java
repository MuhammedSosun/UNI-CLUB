package com.uniClub.mapper.eventMapper;

import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.event.eventEntity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "clubs", ignore = true)              // servis set edecek
    @Mapping(target = "participantCount", ignore = true)   // servis set edecek
    @Mapping(target = "eventParticipations", ignore = true) // DTO'dan gelmiyor
    Event toEventEntity(EventRequest eventRequest);

    @Mapping(target = "clubIds",
            expression = "java(event.getClubs() == null ? null : " +
                    "event.getClubs().stream().map(c -> c.getId()).collect(java.util.stream.Collectors.toSet()))")
    EventResponse toEventResponse(Event event);
}
