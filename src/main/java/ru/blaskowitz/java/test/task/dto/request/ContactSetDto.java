package ru.blaskowitz.java.test.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactSetDto {
    @NotEmpty(message = "Emails cannot be empty")
    private Set <@Email(message = "Invalid email format")
            @Size(max = 200)
            String> emails;

    @NotEmpty(message = "Phones cannot be empty")
    private Set <@Pattern(
            regexp = "^(7)[0-9]{9,14}$",
            message = "Invalid phone number format. Phone number must start with 7")
            String> phones;
}
