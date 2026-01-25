package com.uniClub.member.memberDto;

import com.uniClub.enums.ClubMembershipStatus;

public record UpdateMemberStatusRequest(
        ClubMembershipStatus status
) {}