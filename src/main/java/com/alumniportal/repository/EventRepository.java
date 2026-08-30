package com.alumniportal.repository;

import com.alumniportal.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // Search events by title
    List<Event> findByTitleContainingIgnoreCase(String title);

    // Search events by location
    List<Event> findByLocationContainingIgnoreCase(String location);

    // Search events by organizer
    List<Event> findByOrganizerContainingIgnoreCase(String organizer);
}