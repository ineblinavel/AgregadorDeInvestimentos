package com.luanr.agregadorinvestimentos.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UpdateUserDto(
        @Size(min = 3)
        @NotBlank
        String username,
        @Size(min = 6)
        @NotBlank
        String password
) {
}