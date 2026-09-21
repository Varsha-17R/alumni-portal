package com.alumniportal.service;

import com.alumniportal.entity.Profile;
import com.alumniportal.entity.User;
import com.alumniportal.repository.ProfileRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EntityManager entityManager;

    public ProfileService(
            ProfileRepository profileRepository,
            EntityManager entityManager) {

        this.profileRepository = profileRepository;
        this.entityManager = entityManager;
    }

    // =========================================================
    // SAVE PROFILE
    // =========================================================

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    // =========================================================
    // GET ALL PROFILES
    // =========================================================

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    // =========================================================
    // GET PROFILE BY ID
    // =========================================================

    public Profile getProfileById(Long id) {

        Optional<Profile> profile =
                profileRepository.findById(id);

        return profile.orElse(null);
    }

    // =========================================================
    // GET PROFILE BY USER ID
    // =========================================================

    public Profile getProfileByUserId(Long userId) {

        Optional<Profile> profile =
                profileRepository.findByUserId(userId);

        return profile.orElse(null);
    }

    // =========================================================
    // GET OR CREATE PROFILE
    // =========================================================

    public Profile getOrCreateProfileByUserId(Long userId) {

        Optional<Profile> existingProfile =
                profileRepository.findByUserId(userId);

        if (existingProfile.isPresent()) {
            return existingProfile.get();
        }

        User user =
                entityManager.getReference(
                        User.class,
                        userId
                );

        Profile profile = new Profile();

        profile.setUser(user);
        profile.setEducation("");
        profile.setGraduationYear("");
        profile.setCompany("");
        profile.setJobTitle("");
        profile.setSkills("");
        profile.setLocation("");

        return profileRepository.save(profile);
    }

    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    public Profile updateProfile(
            Long id,
            Profile updatedProfile) {

        Profile existingProfile =
                profileRepository.findById(id)
                        .orElse(null);

        if (existingProfile == null) {
            return null;
        }

        existingProfile.setEducation(
                updatedProfile.getEducation()
        );

        existingProfile.setGraduationYear(
                updatedProfile.getGraduationYear()
        );

        existingProfile.setCompany(
                updatedProfile.getCompany()
        );

        existingProfile.setJobTitle(
                updatedProfile.getJobTitle()
        );

        existingProfile.setSkills(
                updatedProfile.getSkills()
        );

        existingProfile.setLocation(
                updatedProfile.getLocation()
        );

        /*
         * IMPORTANT:
         * We intentionally do NOT update the photo here.
         *
         * Photo is managed separately through:
         * POST   /api/profiles/{id}/photo
         * GET    /api/profiles/{id}/photo
         * DELETE /api/profiles/{id}/photo
         */

        return profileRepository.save(existingProfile);
    }

    // =========================================================
    // UPLOAD PROFILE PHOTO
    // =========================================================

    public Profile uploadPhoto(
            Long id,
            MultipartFile file) throws IOException {

        Profile profile =
                profileRepository.findById(id)
                        .orElse(null);

        if (profile == null) {
            return null;
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select an image"
            );
        }

        // Maximum file size: 5 MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Image size must be less than 5 MB"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !contentType.startsWith("image/")) {

            throw new IllegalArgumentException(
                    "Only image files are allowed"
            );
        }

        profile.setPhoto(file.getBytes());
        profile.setPhotoContentType(contentType);

        return profileRepository.save(profile);
    }

    // =========================================================
    // GET PROFILE PHOTO
    // =========================================================

    public Profile getProfilePhoto(Long id) {

        return profileRepository.findById(id)
                .orElse(null);
    }

    // =========================================================
    // DELETE PROFILE PHOTO
    // =========================================================

    public boolean deletePhoto(Long id) {

        Profile profile =
                profileRepository.findById(id)
                        .orElse(null);

        if (profile == null) {
            return false;
        }

        profile.setPhoto(null);
        profile.setPhotoContentType(null);

        profileRepository.save(profile);

        return true;
    }

    // =========================================================
    // DELETE PROFILE
    // =========================================================

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }
}