package ru.blaskowitz.java.test.task.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.blaskowitz.java.test.task.dto.request.FilterDto;
import ru.blaskowitz.java.test.task.model.User;

public class UserSpecificationBuilder {

    public static Specification<User> build(FilterDto filter) {
        return Specification.where(UserSpecification.hasNameLike(filter.getName()))
                                           .and(UserSpecification.hasDateOfBirthAfter(filter.getDateOfBirth()))
                                           .and(UserSpecification.hasEmail(filter.getEmail()))
                                           .and(UserSpecification.hasPhone(filter.getPhone()));
    }
}
