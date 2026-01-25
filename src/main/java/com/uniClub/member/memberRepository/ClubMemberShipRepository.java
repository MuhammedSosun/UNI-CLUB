package com.uniClub.repository.memberRepository;

import com.uniClub.entity.memberEntity.ClubMembership;
import com.uniClub.enums.ClubMembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubMemberShipRepository extends JpaRepository<ClubMembership,Long> {

    Optional<ClubMembership> findByClubIdAndMemberId(Long clubId,Long memberId);
    Optional<ClubMembership> findByClubId(Long clubId);
    Optional<ClubMembership> findByMemberId(Long memberId);


    List<ClubMembership> findAllByClubIdAndStatus(Long clubId, ClubMembershipStatus status);

    List<ClubMembership> findAllByClubId(Long clubId);

    boolean existsByClubIdAndMemberId(Long clubId,Long memberId);
}
