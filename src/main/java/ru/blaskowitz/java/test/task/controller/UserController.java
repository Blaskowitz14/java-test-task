package ru.blaskowitz.java.test.task.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.blaskowitz.java.test.task.dto.request.ContactSetDto;
import ru.blaskowitz.java.test.task.dto.request.FilterDto;
import ru.blaskowitz.java.test.task.dto.response.UserDto;
import ru.blaskowitz.java.test.task.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/contacts")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody ContactSetDto contactSetDto,
                                              @Parameter(hidden = true) @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.updateContacts(userId, contactSetDto));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<UserDto>> searchUsers(Pageable pageable, FilterDto filterDto) {
        return ResponseEntity.ok(userService.findAll(pageable, filterDto));
    }
}
