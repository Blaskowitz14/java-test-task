package ru.blaskowitz.java.test.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.blaskowitz.java.test.task.dto.request.ContactSetDto;
import ru.blaskowitz.java.test.task.dto.request.FilterDto;
import ru.blaskowitz.java.test.task.dto.response.UserDto;
import ru.blaskowitz.java.test.task.exception.DuplicateContactException;
import ru.blaskowitz.java.test.task.exception.NotFoundException;
import ru.blaskowitz.java.test.task.mapper.UserMapper;
import ru.blaskowitz.java.test.task.model.EmailData;
import ru.blaskowitz.java.test.task.model.PhoneData;
import ru.blaskowitz.java.test.task.model.User;
import ru.blaskowitz.java.test.task.repository.EmailRepository;
import ru.blaskowitz.java.test.task.repository.PhoneRepository;
import ru.blaskowitz.java.test.task.repository.UserRepository;
import ru.blaskowitz.java.test.task.specification.UserSpecificationBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserDto getUserById(Long userId) {
        log.info("Fetching user with id: {}", userId);
        return userMapper.toUserDto(findUserById(userId));
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public UserDto updateContacts(Long userId, ContactSetDto contactSetDto) {
        log.info("Updating contacts for user {} with emails {} and phones {}",
                 userId, contactSetDto.getEmails(), contactSetDto.getPhones());
        Set<String> emails = contactSetDto.getEmails();
        Set<String> phones = contactSetDto.getPhones();

        emails.forEach(email -> {
            if(isEmailUnavailable(email, userId)) {
                throw new DuplicateContactException("email " + email + " is already in use");
            }
        });
        phones.forEach(phone -> {
            if(isPhoneUnavailable(phone, userId)) {
                throw new DuplicateContactException("phone " + phone + " is already in use");
            }
        });

        User user = findUserById(userId);
        updateEmails(user, emails);
        updatePhones(user, phones);
        log.info("Contacts updated for user {}", userId);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmailOrPhone(String login) {
        Optional<User> user;
        if (login.contains("@")) {
            user = userRepository.findByEmail(login);
        } else {
            user = userRepository.findByPhone(login);
        }
        return getUserOrThrow(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable, FilterDto filterDto) {
        log.info("Fetching users with filter: {}", filterDto);
        Specification<User> spec = UserSpecificationBuilder.build(filterDto);
        return userRepository.findAll(spec, pageable)
                .map(userMapper::toUserDto);
    }

    private User findUserById(Long userId) {
        return getUserOrThrow(userRepository.findById(userId));
    }

    private User getUserOrThrow(Optional<User> user) {
        return user.orElseThrow(() -> new NotFoundException("user not found"));
    }

    private boolean isEmailUnavailable(String email, Long userId) {
        boolean unavailable = emailRepository.existsByEmailAndUserIdNot(email, userId);
        if (unavailable) {
            log.error("Email {} is unavailable for user {}", email, userId);
        }
        return unavailable;
    }

    private boolean isPhoneUnavailable(String phone, Long userId) {
        boolean unavailable = phoneRepository.existsByPhoneAndUserIdNot(phone, userId);
        if (unavailable) {
            log.error("Phone {} is unavailable for user {}", phone, userId);
        }
        return unavailable;
    }

    private void updateEmails(User user, Set<String> newEmails) {
        user.getEmails().removeIf(emailData -> !newEmails.contains(emailData.getEmail()));
        Set<String> currentEmails = user.getEmails().stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toSet());
        newEmails.stream()
                .filter(email -> !currentEmails.contains(email))
                .forEach(email -> {
                    EmailData emailData = new EmailData();
                    emailData.setEmail(email);
                    emailData.setUser(user);
                    user.getEmails().add(emailData);
                });
    }

    private void updatePhones(User user, Set<String> newPhones) {
        user.getPhones().removeIf(phoneData -> !newPhones.contains(phoneData.getPhone()));
        Set<String> currentPhones = user.getPhones().stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toSet());
        newPhones.stream()
                .filter(phone -> !currentPhones.contains(phone))
                .forEach(phone -> {
                    PhoneData phoneData = new PhoneData();
                    phoneData.setPhone(phone);
                    phoneData.setUser(user);
                    user.getPhones().add(phoneData);
                });
    }
}

