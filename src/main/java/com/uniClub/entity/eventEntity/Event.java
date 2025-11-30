package com.uniClub.entity.eventEntity;

import com.uniClub.entity.baseEntity.BaseEntity;
import com.uniClub.entity.clubEntity.ClubEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "location")
    private String location;

    @ElementCollection
    @CollectionTable(name = "event_participants", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "participant_id")
    private Set<UUID> participantIds = new HashSet<>();

    private int participantCount = 0;


    @ManyToMany
    @JoinTable(
            name = "event_clubs",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private Set<ClubEntity> clubs = new HashSet<>();




}
