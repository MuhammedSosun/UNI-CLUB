package com.uniClub.member.memberService;

import com.uniClub.member.memberDto.MemberRequest;
import com.uniClub.member.memberDto.MemberResponse;
import com.uniClub.user.userEntity.UserEntity;

public interface IMemberService {
    MemberResponse getMyProfile(UserEntity user);
    MemberResponse updateMyProfile(MemberRequest memberRequest,UserEntity user);
    public Long activeMemberCount();
}
