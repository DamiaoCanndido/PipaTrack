package br.com.prestcontas.pipatrack.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.prestcontas.pipatrack.dto.LoginRequest;
import br.com.prestcontas.pipatrack.dto.LoginResponse;
import br.com.prestcontas.pipatrack.dto.RegisterUserDTO;
import br.com.prestcontas.pipatrack.dto.UpdateUserDTO;
import br.com.prestcontas.pipatrack.dto.UserDTO;
import br.com.prestcontas.pipatrack.services.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterUserDTO dto) {
        userService.register(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<UserDTO> listUsers(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(userService.listUsers(page, pageSize));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PatchMapping("/user/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> updateUser(@PathVariable("id") UUID userId, @Valid @RequestBody UpdateUserDTO dto){
        userService.updateUser(userId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID userId, JwtAuthenticationToken token){
        userService.deleteUser(userId, token);
        return ResponseEntity.ok().build();
    }
}
