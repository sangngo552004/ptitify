package com.sangngo552004.musicapp.repository;

import com.sangngo552004.musicapp.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@Stateless
public class UserRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        }
        return entityManager.merge(user);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public Optional<User> findByEmail(String email) {
        return findSingle("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)", "email", email);
    }

    public Optional<User> findByUsername(String username) {
        return findSingle("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)", "username", username);
    }

    public Optional<User> findByGoogleAccountId(String googleAccountId) {
        return findSingle(
                "SELECT u FROM User u WHERE u.googleAccountId = :googleAccountId",
                "googleAccountId",
                googleAccountId
        );
    }

    public Optional<User> findByEmailOrUsername(String identifier) {
        try {
            User user = entityManager.createQuery(
                            "SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:identifier) " +
                                    "OR LOWER(u.username) = LOWER(:identifier)",
                            User.class
                    )
                    .setParameter("identifier", identifier)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public boolean existsByEmail(String email) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE LOWER(u.email) = LOWER(:email)",
                        Long.class
                )
                .setParameter("email", email)
                .getSingleResult();
        return count != null && count > 0;
    }

    public boolean existsByUsername(String username) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE LOWER(u.username) = LOWER(:username)",
                        Long.class
                )
                .setParameter("username", username)
                .getSingleResult();
        return count != null && count > 0;
    }

    private Optional<User> findSingle(String jpql, String parameterName, String value) {
        try {
            User user = entityManager.createQuery(jpql, User.class)
                    .setParameter(parameterName, value)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
