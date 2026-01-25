package com.uniClub.dto.memberDto;

import com.uniClub.enums.ClubRole;

public record UpdateMemberRoleRequest(
        ClubRole role
) {}
