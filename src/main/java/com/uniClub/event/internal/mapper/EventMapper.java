package com.uniClub.event.internal.mapper;

import com.uniClub.event.api.dto.EventRequest;
import com.uniClub.event.api.dto.EventResponse;
import com.uniClub.event.internal.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEventEntity(EventRequest eventRequest);

    EventResponse toEventResponse(Event event);
}
