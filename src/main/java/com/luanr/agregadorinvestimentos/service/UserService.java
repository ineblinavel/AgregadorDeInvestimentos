package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.dto.responses.AccountResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateAccountDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateUserDto;
import com.luanr.agregadorinvestimentos.dto.requests.UpdateUserDto;
import com.luanr.agregadorinvestimentos.dto.responses.UserResponseDto;
import com.luanr.agregadorinvestimentos.entity.Role;
import com.luanr.agregadorinvestimentos.entity.User;
import com.luanr.agregadorinvestimentos.mapper.AccountMapper;
import com.luanr.agregadorinvestimentos.mapper.UserMapper;
import com.luanr.agregadorinvestimentos.repository.AccountRepository;
import com.luanr.agregadorinvestimentos.repository.RoleRepository;
import com.luanr.agregadorinvestimentos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;

    public UserService(UserRepository userRepository, AccountRepository accountRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            RoleRepository roleRepository, UserMapper userMapper, AccountMapper accountMapper) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
    }

    @Transactional
    public UUID createUser(CreateUserDto createUserDto) {
        if (userRepository.existsByUsername(createUserDto.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        Role role = roleRepository.findById(Role.Values.BASIC.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        User Entity = userMapper.toEntity(createUserDto, bCryptPasswordEncoder, role);
        return userRepository.save(Entity).getUser_id();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(UUID.fromString(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoles().clear();
        user.getAccounts().clear();
        userRepository.save(user);
        userRepository.delete(user);
    }

    @Transactional
    public void updateUser(String id, UpdateUserDto updateUserDto) {
        var userEntity = userRepository.findById(UUID.fromString(id));
        if (userEntity.isPresent()) {
            var user = userEntity.get();
            if (updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
                user.setUpdated_at(Instant.now());
            }
            if (updateUserDto.password() != null) {
                user.setPassword(bCryptPasswordEncoder.encode(updateUserDto.password()));
                user.setUpdated_at(Instant.now());
            }
            userRepository.save(user);

        }
    }

    public void createAccount(String id, CreateAccountDto createAccountDto) {
        var user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var account = accountMapper.toEntity(createAccountDto, user);
        accountRepository.save(account);
    }

    public List<AccountResponseDto> getAccountById(String id) {
        var user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getAccounts().stream().map(accountMapper::toResponseDto).toList();
    }

    @Transactional
    public UserResponseDto getMe(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public void selectActiveAccount(UUID userId, UUID accountId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        boolean accountExists = user.getAccounts().stream()
                .anyMatch(account -> account.getAccount_id().equals(accountId));
        if (!accountExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account dont belong to user");
        }
        user.setActive_account_id(accountId);
        userRepository.save(user);
    }
}
