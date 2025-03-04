package ru.blaskowitz.java.test.task.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.blaskowitz.java.test.task.model.EmailData;
import ru.blaskowitz.java.test.task.model.PhoneData;
import ru.blaskowitz.java.test.task.model.User;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.time.LocalDate;

public class UserSpecification {

    private UserSpecification() {
        throw new IllegalStateException("Instantiation of 'UserSpecification' is not allowed.");
    }

    public static Specification<User> hasNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                        name.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasDateOfBirthAfter(LocalDate dateOfBirth) {
        return (root, query, criteriaBuilder) -> {
            if (dateOfBirth == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("dateOfBirth"), dateOfBirth);
        };
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<User, EmailData> emailJoin = root.join("emails", JoinType.INNER);
            return criteriaBuilder.equal(emailJoin.get("email"), email);
        };
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, criteriaBuilder) -> {
            if (phone == null || phone.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<User, PhoneData> phoneJoin = root.join("phones", JoinType.INNER);
            return criteriaBuilder.equal(phoneJoin.get("phone"), phone);
        };
    }
}
