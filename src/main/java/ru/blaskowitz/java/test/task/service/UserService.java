package ru.blaskowitz.java.test.task.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.blaskowitz.java.test.task.dto.request.ContactSetDto;
import ru.blaskowitz.java.test.task.dto.request.FilterDto;
import ru.blaskowitz.java.test.task.dto.response.UserDto;
import ru.blaskowitz.java.test.task.model.User;

public interface UserService {

    UserDto getUserById(Long userId);

    UserDto updateContacts(Long userId, ContactSetDto contactSetDto);

    User findByEmailOrPhone(String login);

    Page<UserDto> findAll(Pageable pageable, FilterDto filterDto);
}
