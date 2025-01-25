package com.luanr.agregadorinvestimentos.mapper;

import com.luanr.agregadorinvestimentos.dto.requests.CreateUserDto;
import com.luanr.agregadorinvestimentos.dto.responses.UserResponseDto;
import com.luanr.agregadorinvestimentos.entity.Account;
import com.luanr.agregadorinvestimentos.entity.Role;
import com.luanr.agregadorinvestimentos.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
public class UserMapper {

    AccountMapper accountMapper;


    public User toEntity(CreateUserDto createUserDto, BCryptPasswordEncoder encoder, Role role) {
        return new User(
                null,
                createUserDto.username(),
                createUserDto.email(),
                encoder.encode(createUserDto.password()),
                Instant.now(),
                null,
                Set.of(role)
        );
    }

    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getUsername(),
                user.getEmail(),
                user.getCreated_at(),
                user.getUpdated_at(),
                user.getAccounts().stream()
                        .map(accountMapper::toResponseDto)
                        .toList()
        );
    }
}
