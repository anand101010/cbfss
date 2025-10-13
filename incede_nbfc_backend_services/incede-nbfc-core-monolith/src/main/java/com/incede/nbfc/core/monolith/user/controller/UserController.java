package com.incede.nbfc.core.monolith.user.controller;

import com.incede.nbfc.core.monolith.user.dto.UserResponseDto;
import com.incede.nbfc.core.monolith.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{identity}")
    public ResponseEntity<UserResponseDto> getUserByIdentity(@PathVariable UUID identity) {
        return ResponseEntity.ok(userService.getUserByIdentity(identity));
    }

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
