package com.sangngo552004.musicapp.repository;

import com.sangngo552004.musicapp.entity.RefreshToken;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@Stateless
public class RefreshTokenRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public RefreshToken save(RefreshToken refreshToken) {
        if (refreshToken.getId() == null) {
            entityManager.persist(refreshToken);
            return refreshToken;
        }
        return entityManager.merge(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        try {
            RefreshToken refreshToken = entityManager.createQuery(
                            "SELECT rt FROM RefreshToken rt JOIN FETCH rt.user WHERE rt.token = :token",
                            RefreshToken.class
                    )
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.of(refreshToken);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public int revokeByUserId(Long userId) {
        return entityManager.createQuery(
                        "UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId AND rt.revoked = false"
                )
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
