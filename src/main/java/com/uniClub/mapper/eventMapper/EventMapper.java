package com.uniClub.mapper.eventMapper;

import com.uniClub.dto.eventDto.EventRequest;
import com.uniClub.dto.eventDto.EventResponse;
import com.uniClub.entity.eventEntity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEventEntity(EventRequest eventRequest);

    EventResponse toEventResponse(Event event);
}
