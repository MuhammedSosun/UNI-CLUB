package com.uniClub.event.eventDto;

import com.uniClub.enums.ParticipationStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventParticipantDto {
    private Long memberId;
    private String username;
    private String email;
    private ParticipationStatus status;
    private LocalDateTime joinedAt;
}