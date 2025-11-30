package com.uniClub.dto.eventDto;

import com.uniClub.entity.baseEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse extends BaseEntity {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime eventDate;

    private String location;

    private int participantCount;

    private Set<UUID> participantIds;
    private Set<Long> clubIds;
}
