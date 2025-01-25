package com.luanr.agregadorinvestimentos.dto.responses;

import com.luanr.agregadorinvestimentos.entity.Account;

import java.time.Instant;
import java.util.List;

public record UserResponseDto(String username, String email, Instant createdAt, Instant updatedAt, List<AccountResponseDto> accounts) {


}
