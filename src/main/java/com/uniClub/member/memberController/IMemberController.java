package com.uniClub.controller.memberController;

import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.memberDto.MemberRequest;
import com.uniClub.dto.memberDto.MemberResponse;
import com.uniClub.entity.userEntity.UserEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface IMemberController {
    RootEntity<MemberResponse> getMyProfile(UserEntity user);
    RootEntity<MemberResponse> updateMyProfile(MemberRequest member, UserEntity user);

}
