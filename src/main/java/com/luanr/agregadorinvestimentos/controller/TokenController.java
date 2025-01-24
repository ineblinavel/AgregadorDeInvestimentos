package com.luanr.agregadorinvestimentos.controller;


import com.luanr.agregadorinvestimentos.dto.LoginRequest;
import com.luanr.agregadorinvestimentos.dto.LoginResponse;
import com.luanr.agregadorinvestimentos.entity.Role;
import com.luanr.agregadorinvestimentos.repository.UserRepository;
import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.username());
        if(user.isEmpty() || !user.get().islogincorrect(loginRequest, bCryptPasswordEncoder)){
            throw new BadCredentialsException("Invalid username or password");
        }
        var now = Instant.now();

        var scope = user.get().getRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.joining(" "));

        var expiresIn = 450L;
        var claim = JwtClaimsSet.builder()
                .issuer("agregador-investimentos")
                .subject(user.get().getUser_id().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scope)
                .issuedAt(now)
                .build();
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claim)).getTokenValue();
        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}




