package com.uniClub.service.memberService;

import com.uniClub.dto.memberDto.JoinRequestDto;
import com.uniClub.entity.memberEntity.ClubMembership;

import java.util.List;

public interface IClubMembershipService {

    public void requestJoin(Long clubId, String username);

    public List<JoinRequestDto> listPendingRequests(Long clubId,String adminUsername);

    public void approveRequest(Long clubId, Long memberId,String username);

    public void rejectRequest(Long clubId, Long memberId,String username);



}
