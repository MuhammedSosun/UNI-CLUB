package com.uniClub.member.memberService;

import com.uniClub.member.memberDto.MemberRequest;
import com.uniClub.member.memberDto.MemberResponse;
import com.uniClub.user.userEntity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMemberService {
    MemberResponse getMyProfile(UserEntity user);
    MemberResponse updateMyProfile(MemberRequest memberRequest,UserEntity user);
    public Long activeMemberCount();
    Page<MemberResponse> getAllMembers(Pageable pageable, String filter);
    MemberResponse getMemberById(Long id);
}
