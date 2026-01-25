package com.uniClub.member.memberService;

import com.uniClub.member.memberDto.ClubMemberDto;
import com.uniClub.member.memberDto.JoinRequestDto;
import com.uniClub.enums.ClubMembershipStatus;
import com.uniClub.enums.ClubRole;

import java.util.List;

public interface IClubMembershipService {

    public void requestJoin(Long clubId, String username);

    public List<JoinRequestDto> listPendingRequests(Long clubId,String adminUsername);

    public void approveRequest(Long clubId, Long memberId,String username);
    List<ClubMemberDto> listApprovedMembers(Long clubId, String adminUsername);
    List<ClubMemberDto> listClubMembers(Long clubId, String adminUsername);
    public void rejectRequest(Long clubId, Long memberId,String username);
    void leaveClub(Long clubId, String username);
    public void updateMemberRole(Long clubId,
                                 Long targetMemberId,
                                 ClubRole newRole,
                                 String adminUsername);
    public void updateMemberStatus(
            Long clubId,
            Long targetMemberId,
            ClubMembershipStatus status,
            String adminUsername
    );




}
