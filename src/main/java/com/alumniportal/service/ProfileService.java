package com.alumniportal.service;

import com.alumniportal.entity.Profile;
import com.alumniportal.entity.User;
import com.alumniportal.repository.ProfileRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

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

        return profileRepository.save(existingProfile);
    }

    // =========================================================
    // DELETE PROFILE
    // =========================================================

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }
}