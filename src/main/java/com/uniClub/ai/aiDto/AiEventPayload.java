package com.uniClub.ai.aiDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiEventPayload {

    private Long eventId;

    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String location;

    private Integer participantCount;

    private List<String> organizerClubNames;
}