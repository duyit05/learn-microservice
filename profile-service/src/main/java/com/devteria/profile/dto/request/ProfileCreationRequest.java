package com.devteria.profile.dto.request;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCreationRequest {
    private String userId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String city;
}
