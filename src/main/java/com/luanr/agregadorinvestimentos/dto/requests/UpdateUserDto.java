package com.luanr.agregadorinvestimentos.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UpdateUserDto(
        @Size(min = 3, message = "O nome de usuário deve ter pelo menos 3 caracteres")
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        @NotBlank(message = "A senha é obrigatória")
        String password
) {}