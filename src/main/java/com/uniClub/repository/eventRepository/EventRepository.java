package com.uniClub.repository.eventRepository;

import com.uniClub.entity.eventEntity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByEventDateAsc();

    List<Event> findByTitleContainingIgnoreCaseOrderByEventDateAsc(String filter);

}
