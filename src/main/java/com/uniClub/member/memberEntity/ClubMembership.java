package com.uniClub.entity.memberEntity;

import com.uniClub.entity.baseEntity.BaseEntity;
import com.uniClub.Club.clubEntity.ClubEntity;
import com.uniClub.enums.ClubMembershipStatus;
import com.uniClub.enums.ClubRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "club_memberships",
uniqueConstraints = @UniqueConstraint(columnNames = {"club_id", "member_id"}))
public class ClubMembership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id",nullable = false)
    private ClubEntity  club;

    @ManyToOne
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "uniclub.club_role_enum")
    private ClubRole role;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "uniclub.club_membership_status_enum")
    private ClubMembershipStatus status = ClubMembershipStatus.PENDING;

    private LocalDate joinedAt;

    private LocalDate leftAt;

    private LocalDate requestedAt;


}
