package com.luanr.agregadorinvestimentos.controller;


import com.luanr.agregadorinvestimentos.dto.responses.AccountResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateAccountDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateUserDto;
import com.luanr.agregadorinvestimentos.dto.requests.UpdateUserDto;
import com.luanr.agregadorinvestimentos.entity.User;
import com.luanr.agregadorinvestimentos.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDto createUserDto){
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/users/" + userId.toString())).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable String id){
        var user = userService.getUser(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("me")
    public ResponseEntity<Void> updateUserbyId(@Valid @RequestBody UpdateUserDto updateUserDto , JwtAuthenticationToken token){
        userService.updateUser(token.getName(), updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(JwtAuthenticationToken token){
        userService.deleteUser(token.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/accounts")
    public ResponseEntity<Void> createAccount(@Valid @RequestBody CreateAccountDto createAccountDto, JwtAuthenticationToken token){
        userService.createAccount(token.getName(), createAccountDto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me/accounts")
    public ResponseEntity<List<AccountResponseDto>> getAllaccountsById(JwtAuthenticationToken token){
        var accounts = userService.getAccountById(token.getName());
        return ResponseEntity.ok(accounts);
    }

}
