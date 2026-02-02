package com.uniClub.event.eventDto;

import com.uniClub.enums.ParticipationStatus;
import lombok.Data;

@Data
public class ParticipationStatusRequest {
    private Long memberId; // Hangi üye için işlem yapıyoruz?
    private ParticipationStatus status; // Yeni durum ne olsun? (APPROVED veya REJECTED)
}