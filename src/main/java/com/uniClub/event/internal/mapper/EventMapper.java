package com.uniClub.event.internal.mapper;

import com.uniClub.event.api.dto.EventRequest;
import com.uniClub.event.api.dto.EventResponse;
import com.uniClub.event.internal.entity.Event;
import com.uniClub.user.internal.entity.UserEntity;

public class EventMapper {

    public static Event toEventEntity(EventRequest eventRequest , UserEntity userEntity) {

        if (eventRequest == null) {
            return null;
        }
        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setEventDate(eventRequest.getEventDate());
        event.setLocation(eventRequest.getLocation());
        return event;
    }
    public static EventResponse toEventResponse(Event event){
        if (event == null){
            return null;
        }
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(event.getId());
        eventResponse.setTitle(event.getTitle());
        eventResponse.setDescription(event.getDescription());
        eventResponse.setEventDate(event.getEventDate());
        eventResponse.setLocation(event.getLocation());
        if (event.getCreatedBy() != null) {
            eventResponse.setCreatedByUserName(event.getCreatedBy().getUsername());
        }

        eventResponse.setCreatedAt(event.getCreatedAt());
        eventResponse.setUpdatedAt(event.getUpdatedAt());
        return eventResponse;
    }


}
