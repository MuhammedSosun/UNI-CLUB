package com.uniClub.member.memberDto;

import com.uniClub.enums.ClubMembershipStatus;
import com.uniClub.enums.ClubRole;

import java.time.LocalDate;

public record ClubMemberDto(
        Long memberId,
        String name,
        String surname,
        String studentNumber,
        ClubMembershipStatus status,
        ClubRole role,
        LocalDate joinedAt
) {}
