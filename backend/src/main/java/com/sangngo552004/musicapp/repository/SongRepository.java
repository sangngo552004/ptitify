package com.sangngo552004.musicapp.repository;

import com.sangngo552004.musicapp.entity.Song;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class SongRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public Optional<Song> findByIdWithRelations(Long songId) {
        try {
            Song song = entityManager.createQuery(
                            "SELECT s FROM Song s " +
                                    "JOIN FETCH s.artist " +
                                    "JOIN FETCH s.genre " +
                                    "WHERE s.id = :songId",
                            Song.class
                    )
                    .setParameter("songId", songId)
                    .getSingleResult();
            return Optional.of(song);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public List<Song> findByGenreWithCursor(Long genreId, Long cursor, int limit) {
        String jpql = "SELECT s FROM Song s " +
                "JOIN FETCH s.artist " +
                "JOIN FETCH s.genre " +
                "WHERE s.genre.id = :genreId " +
                (cursor != null ? "AND s.id < :cursor " : "") +
                "ORDER BY s.id DESC";

        TypedQuery<Song> query = entityManager.createQuery(jpql, Song.class)
                .setParameter("genreId", genreId)
                .setMaxResults(limit);

        if (cursor != null) {
            query.setParameter("cursor", cursor);
        }
        return query.getResultList();
    }

    public List<Song> findByArtistWithCursor(Long artistId, Long cursor, int limit) {
        String jpql = "SELECT s FROM Song s " +
                "JOIN FETCH s.artist " +
                "JOIN FETCH s.genre " +
                "WHERE s.artist.id = :artistId " +
                (cursor != null ? "AND s.id < :cursor " : "") +
                "ORDER BY s.id DESC";

        TypedQuery<Song> query = entityManager.createQuery(jpql, Song.class)
                .setParameter("artistId", artistId)
                .setMaxResults(limit);

        if (cursor != null) {
            query.setParameter("cursor", cursor);
        }
        return query.getResultList();
    }
}
