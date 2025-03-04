package ru.blaskowitz.java.test.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Set<String> emails;
    private Set<String> phones;
}
