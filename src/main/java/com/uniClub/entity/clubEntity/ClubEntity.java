package com.uniClub.entity.clubEntity;

import com.uniClub.entity.baseEntity.BaseEntity;
import com.uniClub.entity.eventEntity.Event;
import com.uniClub.entity.userEntity.UserEntity;
import com.uniClub.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "clubs")
@NoArgsConstructor
@AllArgsConstructor
public class ClubEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "club_name")
    private String clubName;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "description")
    private String description;
    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "foundation_date")
    private LocalDate foundationDate;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "instagram")
    private String instagram;

    @OneToOne
    @JoinColumn(name = "president_id")
    private UserEntity president;

    @ManyToMany
    @JoinTable(
            name = "club_members",
            joinColumns =  @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> members = new HashSet<>();

    @ManyToMany(mappedBy = "clubs", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Event>  events = new ArrayList<>();
    @Column(name = "approved")
    private boolean approved;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status;




}
