package com.luanr.agregadorinvestimentos.controller;


import com.luanr.agregadorinvestimentos.dto.AccountResponseDto;
import com.luanr.agregadorinvestimentos.dto.CreateAccountDto;
import com.luanr.agregadorinvestimentos.dto.CreateUserDto;
import com.luanr.agregadorinvestimentos.dto.UpdateUserDto;
import com.luanr.agregadorinvestimentos.entity.User;
import com.luanr.agregadorinvestimentos.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto){
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/users/" + userId.toString())).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable String id){
        var user = userService.getUser(id);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateUserbyId(@PathVariable String id,
                                               @RequestBody UpdateUserDto updateUserDto){
        userService.updateUser(id, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Void> createAccount(@PathVariable String id,
                                           @RequestBody CreateAccountDto createAccountDto){
        userService.createAccount(id, createAccountDto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<AccountResponseDto>> getAllaccountsById(@PathVariable String id){
        var accounts = userService.getAccountById(id);
        return ResponseEntity.ok(accounts);
    }

}
