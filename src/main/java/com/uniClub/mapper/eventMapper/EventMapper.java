package com.uniClub.mapper.eventMapper;

import com.uniClub.dto.eventDto.EventRequest;
import com.uniClub.dto.eventDto.EventResponse;
import com.uniClub.entity.eventEntity.Event;
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
