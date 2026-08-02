package com.devteria.profile.controller;

import org.springframework.web.bind.annotation.*;

import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class InternalUserProfileController {
    private final UserProfileService service;

    @PostMapping("/internal/users")
    public UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request) {
        return service.createProfile(request);
    }
}
