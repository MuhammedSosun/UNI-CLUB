package com.uniClub.ai.aiService;

import com.uniClub.Club.clubEntity.ClubEntity;
import com.uniClub.Club.clubRepository.ClubRepository;
import com.uniClub.ai.aiDto.*;
import com.uniClub.commonmethods.SecurityUtils;
import com.uniClub.event.eventEntity.Event;
import com.uniClub.event.eventRepository.EventRepository;
import com.uniClub.member.memberEntity.Member;
import com.uniClub.member.memberRepository.MemberRepository;
import com.uniClub.user.userEntity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final EventRepository eventRepository;
    private final GeminiService geminiService;

    public AiRecommendationResponse generateRecommendationForCurrentMember() {

        UserEntity currentUser = SecurityUtils.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Authenticated user not found");
        }

        Member member = memberRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Member profile not found"));

        List<ClubEntity> clubs = clubRepository.findAll();

        List<Event> events = eventRepository
                .findUpcomingEventsPaged(LocalDateTime.now(), PageRequest.of(0, 20))
                .getContent();

        AiRecommendationPayload payload = buildPayload(member, clubs, events);

        return geminiService.generateRecommendation(payload);
    }
    private AiRecommendationPayload buildPayload(
            Member member,
            List<ClubEntity> clubs,
            List<Event> events
    ) {
        AiMemberProfilePayload memberProfile = new AiMemberProfilePayload(
                member.getId(),
                member.getName(),
                member.getSurname(),
                member.getAge(),
                member.getFaculty(),
                member.getDepartment(),
                member.getLevel(),
                member.getUniversity(),
                member.getAbout(),
                member.getSkills(),
                member.getInterests(),
                member.getCertificates(),
                member.getLanguages(),
                member.getProjects()
        );

        List<AiClubPayload> clubPayloads = clubs.stream()
                .filter(ClubEntity::isApproved)
                .map(club -> new AiClubPayload(
                        club.getId(),
                        club.getClubName(),
                        club.getShortName(),
                        club.getDescription(),
                        club.getFoundationDate(),
                        club.isApproved(),
                        club.getStatus() != null ? club.getStatus().name() : null
                ))
                .toList();

        List<AiEventPayload> eventPayloads = events.stream()
                .map(event -> new AiEventPayload(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getEventDate(),
                        event.getLocation(),
                        event.getParticipantCount(),
                        event.getClubs()
                                .stream()
                                .map(ClubEntity::getClubName)
                                .toList()
                ))
                .toList();

        return new AiRecommendationPayload(
                memberProfile,
                clubPayloads,
                eventPayloads
        );
    }
}