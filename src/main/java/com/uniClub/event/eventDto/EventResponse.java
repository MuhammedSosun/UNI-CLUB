package com.uniClub.event.eventDto;

import com.uniClub.baseEntity.BaseEntity;
import com.uniClub.enums.ParticipationStatus;
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

    // Mapper ile uyumlu olması için Set<Long> olarak kalmalı
    private Set<Long> clubIds;

    private boolean Joined;

    private ParticipationStatus participationStatus;
}