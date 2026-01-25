package com.uniClub.member.memberDto;

import com.uniClub.enums.ClubRole;

public record UpdateMemberRoleRequest(
        ClubRole role
) {}
