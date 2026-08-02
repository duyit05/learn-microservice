package com.devteria.profile.dto.response;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
}
