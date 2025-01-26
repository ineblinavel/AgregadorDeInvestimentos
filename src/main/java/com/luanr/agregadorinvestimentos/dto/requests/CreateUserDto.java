package com.luanr.agregadorinvestimentos.dto.requests;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,
        @NotBlank(message = "A senha é obrigatória")
        String password
) {}