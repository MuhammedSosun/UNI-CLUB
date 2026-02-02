package com.uniClub.event.eventRepository;

import com.uniClub.event.eventEntity.Event;
import com.uniClub.event.eventEntity.EventParticipation;
import com.uniClub.member.memberEntity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    boolean existsByEventAndMember(Event event, Member member);
    Optional<EventParticipation> findByEventIdAndMemberId(Long eventId, Long memberId);
    @Query("SELECT ep FROM EventParticipation ep WHERE ep.event.id = :eventId AND " +
            "(:filter IS NULL OR :filter = '' OR " +
            "LOWER(ep.member.user.username) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(ep.member.user.email) LIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<EventParticipation> findParticipantsByEventId(
            @Param("eventId") Long eventId,
            @Param("filter") String filter,
            Pageable pageable
    );
}
