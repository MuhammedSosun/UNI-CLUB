package com.uniClub.member.memberController;

import com.uniClub.util.controller.RootEntity;
import com.uniClub.member.memberDto.MemberRequest;
import com.uniClub.member.memberDto.MemberResponse;
import com.uniClub.user.userEntity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface IMemberController {
    RootEntity<MemberResponse> getMyProfile(UserEntity user);
    RootEntity<MemberResponse> updateMyProfile(MemberRequest member, UserEntity user);
    RootEntity<Long> activeMember();
    RootEntity<Page<MemberResponse>> getAllMembers(int page, int size,String filter);
    RootEntity<MemberResponse> getMemberById(Long id);
}
