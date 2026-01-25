package com.uniClub.member.memberDto;

import com.uniClub.enums.ClubMembershipStatus;
import com.uniClub.enums.ClubRole;

public record JoinRequestDto(
        Long memberId,
        String name,
        String surname,
        String studentNumber,
        ClubRole role,
        ClubMembershipStatus status
) {}
