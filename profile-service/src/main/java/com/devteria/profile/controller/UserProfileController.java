package com.devteria.profile.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;

    @GetMapping("/users/{profileId}")
    public UserProfileResponse getProfile(@PathVariable String profileId) {
        return service.getProfile(profileId);
    }

    @GetMapping("/users")
    public List<UserProfileResponse> getUsersProfile() {
        return service.getUsersProfile();
    }
}
