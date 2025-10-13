package com.incede.nbfc.core.monolith.user.service;

import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.dto.UserResponseDto;
import com.incede.nbfc.core.monolith.user.mapper.UserMapper;
import com.incede.nbfc.core.monolith.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponseDto getUserByIdentity(UUID identity) {
        log.info("Fetching user by identity: {}", identity);
        User user = userRepository.findByIdentity(identity)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", identity.toString()));
        return userMapper.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
