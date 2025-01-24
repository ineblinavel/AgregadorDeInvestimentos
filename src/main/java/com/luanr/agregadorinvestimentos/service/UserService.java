package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.dto.responses.AccountResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateAccountDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateUserDto;
import com.luanr.agregadorinvestimentos.dto.requests.UpdateUserDto;
import com.luanr.agregadorinvestimentos.entity.Account;
import com.luanr.agregadorinvestimentos.entity.BillingAddress;
import com.luanr.agregadorinvestimentos.entity.Role;
import com.luanr.agregadorinvestimentos.entity.User;
import com.luanr.agregadorinvestimentos.repository.AccountRepository;
import com.luanr.agregadorinvestimentos.repository.BillingAddressRepository;
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
    private final BillingAddressRepository billingAddressRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        //DTO -> Entity
        var Entity = new User(
                null,
                createUserDto.username(),
                createUserDto.email(),
                bCryptPasswordEncoder.encode(createUserDto.password()),
                Instant.now(),
                null,
                Set.of(roleRepository.findById(Role.Values.BASIC.getRoleId()).orElseThrow())
        );
        var userSaved = userRepository.save(Entity);
        return userSaved.getUser_id();
    }

    public Optional<User> getUser(String id){
        return userRepository.findById(UUID.fromString(id));
    }

    public List<User> getAllUsers(){
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

    public void updateUser(String id, UpdateUserDto updateUserDto) {
        var userEntity = userRepository.findById(UUID.fromString(id));
        if (userEntity.isPresent()){
                var user = userEntity.get();
                if(updateUserDto.username() != null){
                    user.setUsername(updateUserDto.username());
                    user.setUpdated_at(Instant.now());
                }
                if(updateUserDto.password() != null){
                    user.setPassword(bCryptPasswordEncoder.encode(updateUserDto.password()));
                    user.setUpdated_at(Instant.now());
                }
                userRepository.save(user);

         }
    }


    public void createAccount(String id, CreateAccountDto createAccountDto) {

        var user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        //DTO -> Entity
        var account = new Account(
                null,
                createAccountDto.description(),
                user,
                null,
                new ArrayList<>()
        );


        var billingAddressEntity = new BillingAddress(
                account.getAccount_id(),
                account,
                createAccountDto.street(),
                createAccountDto.number()
        );
        account.setBillingAddress(billingAddressEntity);
        accountRepository.save(account);
    }

    public List<AccountResponseDto> getAccountById(String id) {
        var user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return user.getAccounts()
                .stream().map(ac -> new AccountResponseDto(ac.getAccount_id().toString(), ac.getDescription()))
                .toList();
    }
}
