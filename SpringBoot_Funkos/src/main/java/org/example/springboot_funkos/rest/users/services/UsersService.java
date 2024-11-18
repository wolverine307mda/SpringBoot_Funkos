package org.example.springboot_funkos.rest.users.services;


import org.example.springboot_funkos.rest.users.dto.UserInfoResponse;
import org.example.springboot_funkos.rest.users.dto.UserRequest;
import org.example.springboot_funkos.rest.users.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsersService {

    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    UserInfoResponse findById(Long id);

    UserResponse save(UserRequest userRequest);

    UserResponse update(Long id, UserRequest userRequest);

    void deleteById(Long id);

}
