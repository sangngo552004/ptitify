package com.sangngo552004.musicapp.repository;

import com.sangngo552004.musicapp.entity.FavoriteSong;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class FavoriteSongRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public FavoriteSong save(FavoriteSong favoriteSong) {
        if (favoriteSong.getId() == null) {
            entityManager.persist(favoriteSong);
            return favoriteSong;
        }
        return entityManager.merge(favoriteSong);
    }

    public void delete(FavoriteSong favoriteSong) {
        FavoriteSong managed = entityManager.contains(favoriteSong) ? favoriteSong : entityManager.merge(favoriteSong);
        entityManager.remove(managed);
    }

    public Optional<FavoriteSong> findByUserIdAndSongId(Long userId, Long songId) {
        try {
            FavoriteSong favoriteSong = entityManager.createQuery(
                            "SELECT fs FROM FavoriteSong fs " +
                                    "JOIN FETCH fs.song s " +
                                    "JOIN FETCH s.artist " +
                                    "JOIN FETCH s.genre " +
                                    "WHERE fs.user.id = :userId AND s.id = :songId",
                            FavoriteSong.class
                    )
                    .setParameter("userId", userId)
                    .setParameter("songId", songId)
                    .getSingleResult();
            return Optional.of(favoriteSong);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public List<FavoriteSong> findByUserIdWithCursor(Long userId, Long cursor, int limit) {
        String jpql = "SELECT fs FROM FavoriteSong fs " +
                "JOIN FETCH fs.song s " +
                "JOIN FETCH s.artist " +
                "JOIN FETCH s.genre " +
                "WHERE fs.user.id = :userId " +
                (cursor != null ? "AND fs.id < :cursor " : "") +
                "ORDER BY fs.id DESC";

        TypedQuery<FavoriteSong> query = entityManager.createQuery(jpql, FavoriteSong.class)
                .setParameter("userId", userId)
                .setMaxResults(limit);

        if (cursor != null) {
            query.setParameter("cursor", cursor);
        }
        return query.getResultList();
    }
}
