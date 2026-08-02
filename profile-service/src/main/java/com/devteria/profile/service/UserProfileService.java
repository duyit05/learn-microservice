package com.devteria.profile.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper mapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = mapper.toUserProfile(request);
        return mapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return mapper.toUserProfileResponse(userProfile);
    }

    public List<UserProfileResponse> getUsersProfile() {
        return userProfileRepository.findAll().stream()
                .map(mapper::toUserProfileResponse)
                .toList();
    }
}
