package com.alumniportal.controller;

import com.alumniportal.entity.Profile;
import com.alumniportal.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // =========================================================
    // UPLOAD PROFILE PHOTO
    // =========================================================

    @PostMapping(
            value = "/{id}/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {

            // Check profile
            Profile profile =
                    profileService.getProfileById(id);

            if (profile == null) {

                return ResponseEntity
                        .notFound()
                        .build();

            }

            // Check file
            if (file == null || file.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Please select an image.");

            }

            // Check image type
            String contentType =
                    file.getContentType();

            if (contentType == null ||
                    !contentType.startsWith("image/")) {

                return ResponseEntity
                        .badRequest()
                        .body("Only image files are allowed.");

            }

            // Maximum 5 MB
            if (file.getSize() > 5 * 1024 * 1024) {

                return ResponseEntity
                        .badRequest()
                        .body("Image size must be less than 5 MB.");

            }

            // Save image
            profile.setPhoto(
                    file.getBytes()
            );

            profile.setPhotoContentType(
                    contentType
            );

            Profile savedProfile =
                    profileService.saveProfile(profile);

            return ResponseEntity.ok(savedProfile);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Unable to upload photo: "
                                    + e.getMessage()
                    );
        }
    }

    // =========================================================
    // GET PROFILE PHOTO
    // =========================================================

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhoto(
            @PathVariable Long id) {

        Profile profile =
                profileService.getProfileById(id);

        if (profile == null ||
                profile.getPhoto() == null ||
                profile.getPhoto().length == 0) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        String contentType =
                profile.getPhotoContentType();

        MediaType mediaType =
                MediaType.APPLICATION_OCTET_STREAM;

        try {

            if (contentType != null) {
                mediaType =
                        MediaType.parseMediaType(
                                contentType
                        );
            }

        } catch (Exception e) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(profile.getPhoto());
    }

    // =========================================================
    // DELETE PROFILE PHOTO
    // =========================================================

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<?> deletePhoto(
            @PathVariable Long id) {

        Profile profile =
                profileService.getProfileById(id);

        if (profile == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        profile.setPhoto(null);
        profile.setPhotoContentType(null);

        profileService.saveProfile(profile);

        return ResponseEntity
                .ok()
                .body("Profile photo removed successfully.");
    }
}