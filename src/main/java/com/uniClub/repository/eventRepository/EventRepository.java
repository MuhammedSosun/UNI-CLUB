package com.uniClub.repository.eventRepository;

import com.uniClub.entity.eventEntity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
