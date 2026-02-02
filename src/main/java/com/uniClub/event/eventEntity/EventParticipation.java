package com.uniClub.event.eventEntity;

import com.uniClub.baseEntity.BaseEntity;
import com.uniClub.enums.ParticipationStatus;
import com.uniClub.member.memberEntity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_participations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private boolean attended = false;
    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status = ParticipationStatus.PENDING;
}
