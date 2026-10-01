package com.alumniportal.service;

import com.alumniportal.entity.Event;
import com.alumniportal.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // =========================================================
    // CREATE A NEW EVENT
    // =========================================================

    public Event createEvent(Event event) {

        // Every newly created event starts as PENDING
        event.setStatus("PENDING");

        return eventRepository.save(event);
    }

    // =========================================================
    // GET ALL EVENTS
    // =========================================================

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // =========================================================
    // GET EVENT BY ID
    // =========================================================

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    // =========================================================
    // SEARCH EVENTS BY TITLE
    // =========================================================

    public List<Event> searchByTitle(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }

    // =========================================================
    // SEARCH EVENTS BY LOCATION
    // =========================================================

    public List<Event> searchByLocation(String location) {
        return eventRepository.findByLocationContainingIgnoreCase(location);
    }

    // =========================================================
    // SEARCH EVENTS BY ORGANIZER
    // =========================================================

    public List<Event> searchByOrganizer(String organizer) {
        return eventRepository.findByOrganizerContainingIgnoreCase(organizer);
    }

    // =========================================================
    // APPROVE EVENT
    // =========================================================

    public Event approveEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));

        event.setStatus("APPROVED");

        return eventRepository.save(event);
    }

    // =========================================================
    // REJECT EVENT
    // =========================================================

    public Event rejectEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));

        event.setStatus("REJECTED");

        return eventRepository.save(event);
    }

    // =========================================================
    // DELETE EVENT
    // =========================================================

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}