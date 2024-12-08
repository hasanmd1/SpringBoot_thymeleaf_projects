package com.example.libraryapplication.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {

    @NotNull(message = "Current Password Cannot be Empty")
    String currentPassword;

    @NotNull(message = "Password Cannot be Empty")
    String newPassword;

    @NotNull(message = "It should match the new Password")
    String confirmNewPassword;
}
