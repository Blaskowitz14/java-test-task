package ru.blaskowitz.java.test.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {
    private String name;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
}
