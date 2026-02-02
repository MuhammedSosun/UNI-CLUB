package com.uniClub.member.memberController;

import com.uniClub.util.controller.RootEntity;
import com.uniClub.member.memberDto.MemberRequest;
import com.uniClub.member.memberDto.MemberResponse;
import com.uniClub.user.userEntity.UserEntity;

public interface IMemberController {
    RootEntity<MemberResponse> getMyProfile(UserEntity user);
    RootEntity<MemberResponse> updateMyProfile(MemberRequest member, UserEntity user);
    RootEntity<Long> activeMember();
}
