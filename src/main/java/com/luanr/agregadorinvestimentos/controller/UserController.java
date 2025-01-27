package com.luanr.agregadorinvestimentos.controller;

import com.luanr.agregadorinvestimentos.dto.requests.CreateAccountDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateUserDto;
import com.luanr.agregadorinvestimentos.dto.requests.UpdateUserDto;
import com.luanr.agregadorinvestimentos.dto.responses.AccountResponseDto;
import com.luanr.agregadorinvestimentos.dto.responses.UserResponseDto;
import com.luanr.agregadorinvestimentos.entity.User;
import com.luanr.agregadorinvestimentos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operações para gestão de usuários")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Criar novo usuário",
            description = "Registra um novo usuário no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/users/" + userId.toString())).build();
    }

    @Operation(
            summary = "Obter usuário por ID",
            description = "Recupera detalhes de um usuário específico (requer privilégios de ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        var user = userService.getUser(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Listar todos os usuários",
            description = "Recupera todos os usuários cadastrados (requer privilégios de ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários recuperada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Obter dados do usuário logado",
            description = "Recupera informações do usuário autenticado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do usuário recuperados"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(JwtAuthenticationToken token) {
        UserResponseDto response = userService.getMe(UUID.fromString(token.getName()));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualizar dados do usuário",
            description = "Altera informações do usuário logado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dados atualizados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/me")
    public ResponseEntity<Void> updateUserbyId(
            @Valid @RequestBody UpdateUserDto updateUserDto,
            JwtAuthenticationToken token
    ) {
        userService.updateUser(token.getName(), updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Remove permanentemente o usuário logado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(JwtAuthenticationToken token) {
        userService.deleteUser(token.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Criar nova conta",
            description = "Adiciona uma nova conta ao usuário logado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/me/accounts")
    public ResponseEntity<Void> createAccount(
            @Valid @RequestBody CreateAccountDto createAccountDto,
            JwtAuthenticationToken token
    ) {
        userService.createAccount(token.getName(), createAccountDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Listar contas do usuário",
            description = "Recupera todas as contas do usuário logado"
    )
    @ApiResponse(responseCode = "200", description = "Lista de contas recuperada")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me/accounts")
    public ResponseEntity<List<AccountResponseDto>> getAllaccountsById(JwtAuthenticationToken token) {
        var accounts = userService.getAccountById(token.getName());
        return ResponseEntity.ok(accounts);
    }

    @Operation(
            summary = "Selecionar conta ativa",
            description = "Define uma conta como ativa para operações"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta selecionada"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("me/accounts/{accountId}/select")
    public ResponseEntity<Void> selectAccount(
            @PathVariable String accountId,
            JwtAuthenticationToken token
    ) {
        userService.selectActiveAccount(UUID.fromString(token.getName()), UUID.fromString(accountId));
        return ResponseEntity.ok().build();
    }
}