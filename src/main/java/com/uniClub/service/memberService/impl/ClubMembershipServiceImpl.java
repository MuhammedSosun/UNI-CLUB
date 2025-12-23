package com.uniClub.service.memberService.impl;

import com.uniClub.dto.memberDto.JoinRequestDto;
import com.uniClub.entity.clubEntity.ClubEntity;
import com.uniClub.entity.memberEntity.ClubMembership;
import com.uniClub.entity.memberEntity.Member;
import com.uniClub.enums.ClubMembershipStatus;
import com.uniClub.enums.ClubRole;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.repository.clubRepository.ClubRepository;
import com.uniClub.repository.memberRepository.ClubMemberShipRepository;
import com.uniClub.repository.memberRepository.MemberRepository;
import com.uniClub.service.memberService.IClubMembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClubMembershipServiceImpl implements IClubMembershipService {


    private final ClubMemberShipRepository membershipRepo;
    private final ClubRepository clubRepo;
    private final MemberRepository memberRepo;

    public ClubMembershipServiceImpl(ClubMemberShipRepository membershipRepo, ClubRepository clubRepo, MemberRepository memberRepo) {
        this.membershipRepo = membershipRepo;
        this.clubRepo = clubRepo;
        this.memberRepo = memberRepo;
    }

    @Transactional
    @Override
    public void requestJoin(Long clubId, String username) {
        Member member = memberRepo.findByUserUsername(username)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.MEMBER_NOT_FOUND, username)));

        ClubEntity club = clubRepo.findById(clubId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.CLUB_NOT_FOUND,clubId.toString())));
        membershipRepo.findByClubIdAndMemberId(clubId, member.getId()).ifPresent(existing -> {
            switch (existing.getStatus()) {
                case PENDING -> throw new BaseException(new ErrorMessage(MessageType.REQUEST_ALREADY_EXISTS, username));
                case APPROVED ->  throw new BaseException(new ErrorMessage(MessageType.CLUB_ALREADY_APPROVED, username));
                case REJECTED, LEFT -> {
                    existing.setStatus(ClubMembershipStatus.PENDING);
                    existing.setJoinedAt(null);
                    existing.setRequestedAt(LocalDate.now());
                    existing.setLeftAt(null);
                    membershipRepo.save(existing);
                    return;
                }
            }
        });

        ClubMembership cm = new ClubMembership();
        cm.setClub(club);
        cm.setMember(member);
        cm.setStatus(ClubMembershipStatus.PENDING);
        cm.setJoinedAt(null);
        cm.setLeftAt(null);
        cm.setRequestedAt(LocalDate.now());

        membershipRepo.save(cm);
    }

    @Transactional(readOnly = true)
    @Override
    public List<JoinRequestDto> listPendingRequests(Long clubId,String adminUsername) {
        Member admin = memberRepo.findByUserUsername(adminUsername)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.MEMBER_NOT_FOUND, adminUsername + "is not found")));
        assertClubAdmin(clubId,admin.getId());
        return membershipRepo.findAllByClubIdAndStatus(clubId, ClubMembershipStatus.PENDING)
                .stream()
                .map(m -> new JoinRequestDto(
                        m.getMember().getId(),
                        m.getMember().getName(),
                        m.getMember().getSurname(),
                        m.getMember().getStudentNumber(),
                        m.getRole(),
                        m.getStatus()
                )).toList();
    }
    @Transactional
    @Override
    public void approveRequest(Long clubId, Long targetMemberId,String adminUsername) {

        Member admin = memberRepo.findByUserUsername(adminUsername)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.MEMBER_NOT_FOUND, adminUsername + "is not found")));

        assertClubAdmin(clubId, admin.getId());

        ClubMembership req = membershipRepo.findByClubIdAndMemberId(clubId, targetMemberId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.REQUEST_NOT_FOUND,clubId.toString())));
        if (req.getStatus() != ClubMembershipStatus.PENDING){
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION,"Only PENDING requests can be approved"));
        }
        req.setStatus(ClubMembershipStatus.APPROVED);
        req.setJoinedAt(LocalDate.now());
        req.setLeftAt(null);
        if (req.getRole() == null) {
            req.setRole(ClubRole.STANDARD_MEMBER);
        }
        membershipRepo.save(req);
    }
    @Transactional
    @Override
    public void rejectRequest(Long clubId, Long targetMemberId,String adminUsername) {

        Member admin = memberRepo.findByUserUsername(adminUsername)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.MEMBER_NOT_FOUND, adminUsername + "is not found")));

        assertClubAdmin(clubId,admin.getId());
        ClubMembership req = membershipRepo.findByClubIdAndMemberId(clubId, targetMemberId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.REQUEST_NOT_FOUND,clubId.toString())));
        if(req.getStatus() != ClubMembershipStatus.PENDING){
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION,"Only PENDING requests can be rejected"));
        }
        req.setStatus(ClubMembershipStatus.REJECTED);
        req.setJoinedAt(null);
        req.setLeftAt(null);
        membershipRepo.save(req);
    }

    private void assertClubAdmin(Long clubId, Long adminMemberId) {
        ClubMembership adminMemberShip = membershipRepo.findByClubIdAndMemberId(clubId, adminMemberId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.MEMBER_NOT_FOUND,adminMemberId.toString() + "Forbidden: not a club member")));

        if (adminMemberShip.getStatus() != ClubMembershipStatus.APPROVED) {
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION, "Forbidden: membership not approved"));
        }


        ClubRole role = adminMemberShip.getRole();
        if (role == null) {
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION, "Forbidden: role is missing"));
        }
        boolean isADmin =
                role == ClubRole.PRESIDENT ||
                        role == ClubRole.VICE_PRESIDENT ||
                        role == ClubRole.BOARD_MEMBER;
        if (!isADmin) {
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION, "Forbidden: not club admin"));

        }
    }


}
