package ru.blaskowitz.java.test.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.blaskowitz.java.test.task.dto.response.UserDto;
import ru.blaskowitz.java.test.task.model.EmailData;
import ru.blaskowitz.java.test.task.model.PhoneData;
import ru.blaskowitz.java.test.task.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "emails", source = "emails", qualifiedByName = "mapEmails")
    @Mapping(target = "phones", source = "phones", qualifiedByName = "mapPhones")
    UserDto toUserDto(User user);

    @Named("mapEmails")
    static Set<String> mapEmails(Set<EmailData> emails) {
        return emails.stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toSet());
    }

    @Named("mapPhones")
    static Set<String> mapPhones(Set<PhoneData> phones) {
        return phones.stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toSet());
    }
}

