package com.sangngo552004.musicapp.repository;

import com.sangngo552004.musicapp.entity.PasswordResetToken;
import com.sangngo552004.musicapp.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@Stateless
public class PasswordResetTokenRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public PasswordResetToken save(PasswordResetToken token) {
        if (token.getId() == null) {
            entityManager.persist(token);
            return token;
        }
        return entityManager.merge(token);
    }

    public Optional<PasswordResetToken> findActiveToken(String tokenValue) {
        try {
            PasswordResetToken token = entityManager.createQuery(
                            "SELECT prt FROM PasswordResetToken prt " +
                                    "JOIN FETCH prt.user " +
                                    "WHERE prt.token = :token AND prt.used = false",
                            PasswordResetToken.class
                    )
                    .setParameter("token", tokenValue)
                    .getSingleResult();
            return Optional.of(token);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public void markAllAsUsedForUser(User user) {
        entityManager.createQuery(
                        "UPDATE PasswordResetToken prt SET prt.used = true WHERE prt.user = :user AND prt.used = false"
                )
                .setParameter("user", user)
                .executeUpdate();
    }
}
