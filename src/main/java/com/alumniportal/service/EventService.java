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

    // Create a new event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get event by ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    // Search events by title
    public List<Event> searchByTitle(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }

    // Search events by location
    public List<Event> searchByLocation(String location) {
        return eventRepository.findByLocationContainingIgnoreCase(location);
    }

    // Search events by organizer
    public List<Event> searchByOrganizer(String organizer) {
        return eventRepository.findByOrganizerContainingIgnoreCase(organizer);
    }

    // Delete event
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
