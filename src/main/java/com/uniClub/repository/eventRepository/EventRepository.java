package com.uniClub.repository.eventRepository;

import com.uniClub.entity.eventEntity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByEventDateAsc();

    List<Event> findByTitleContainingIgnoreCaseOrderByEventDateAsc(String filter);

    @Query("SELECT e FROM Event e WHERE e.eventDate >= :now ORDER BY e.eventDate ASC")
    Page<Event> findUpcomingEventsPaged(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("""
    SELECT c AS club, Count(e.id) AS eventCount
        FROM Event e
            JOIN e.clubs c
                WHERE e.eventDate >= :startDate
                    GROUP BY c
                        ORDER BY COUNT(e.id) DESC 
    """)
    List<Object[]> findTopActiveClubsLast3Months(LocalDateTime startDate);

    @Query("""
    SELECT e FROM Event e
    WHERE e.eventDate >= :startDate
    AND e.eventDate < :endDate
    ORDER BY e.participantCount DESC
""")
    List<Event> findTopEventsThisMonth(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

}
