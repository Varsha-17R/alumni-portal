package com.alumniportal.controller;

import com.alumniportal.entity.Event;
import com.alumniportal.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Create a new event
    @PostMapping
    public ResponseEntity<Event> createEvent(
            @RequestBody Event event) {

        Event savedEvent =
                eventService.createEvent(event);

        return ResponseEntity.ok(savedEvent);
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {

        return ResponseEntity.ok(
                eventService.getAllEvents()
        );
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(
            @PathVariable Long id) {

        Event event =
                eventService.getEventById(id);

        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(event);
    }

    // Search events by title
    @GetMapping("/search/title")
    public ResponseEntity<List<Event>> searchByTitle(
            @RequestParam String title) {

        return ResponseEntity.ok(
                eventService.searchByTitle(title)
        );
    }

    // Search events by location
    @GetMapping("/search/location")
    public ResponseEntity<List<Event>> searchByLocation(
            @RequestParam String location) {

        return ResponseEntity.ok(
                eventService.searchByLocation(location)
        );
    }

    // Search events by organizer
    @GetMapping("/search/organizer")
    public ResponseEntity<List<Event>> searchByOrganizer(
            @RequestParam String organizer) {

        return ResponseEntity.ok(
                eventService.searchByOrganizer(organizer)
        );
    }

    // Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id) {

        Event event =
                eventService.getEventById(id);

        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        eventService.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }
}