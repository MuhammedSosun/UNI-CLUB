package com.uniClub.service.memberService;

import com.uniClub.dto.memberDto.MemberRequest;
import com.uniClub.dto.memberDto.MemberResponse;
import com.uniClub.entity.userEntity.UserEntity;

public interface IMemberService {
    MemberResponse getMyProfile(UserEntity user);
    MemberResponse updateMyProfile(MemberRequest memberRequest,UserEntity user);

}
