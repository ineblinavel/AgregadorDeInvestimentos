package com.luanr.agregadorinvestimentos.dto.requests;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank String password
) {
}