package com.uniClub.dto.memberDto;

import com.uniClub.enums.ClubMembershipStatus;

public record UpdateMemberStatusRequest(
        ClubMembershipStatus status
) {}