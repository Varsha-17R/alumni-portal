package com.alumniportal.controller;

import com.alumniportal.entity.Profile;
import com.alumniportal.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // =========================================================
    // CREATE PROFILE
    // =========================================================

    @PostMapping
    public ResponseEntity<Profile> createProfile(
            @RequestBody Profile profile) {

        Profile savedProfile =
                profileService.saveProfile(profile);

        return ResponseEntity.ok(savedProfile);
    }

    // =========================================================
    // GET ALL PROFILES
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {

        return ResponseEntity.ok(
                profileService.getAllProfiles()
        );
    }

    // =========================================================
    // GET PROFILE BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(
            @PathVariable Long id) {

        Profile profile =
                profileService.getProfileById(id);

        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(profile);
    }

    // =========================================================
    // GET OR CREATE PROFILE BY USER ID
    // =========================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<Profile> getProfileByUserId(
            @PathVariable Long userId) {

        Profile profile =
                profileService.getOrCreateProfileByUserId(userId);

        return ResponseEntity.ok(profile);
    }

    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Long id,
            @RequestBody Profile profile) {

        Profile updatedProfile =
                profileService.updateProfile(id, profile);

        if (updatedProfile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedProfile);
    }

    // =========================================================
    // DELETE PROFILE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Long id) {

        Profile profile =
                profileService.getProfileById(id);

        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        profileService.deleteProfile(id);

        return ResponseEntity.noContent().build();
    }
}