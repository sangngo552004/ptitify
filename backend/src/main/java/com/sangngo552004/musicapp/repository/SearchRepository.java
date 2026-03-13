package com.sangngo552004.musicapp.repository;

import com.sangngo552004.musicapp.entity.Artist;
import com.sangngo552004.musicapp.entity.Song;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class SearchRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<Artist> searchArtists(String keyword, int limit) {
        return entityManager.createQuery(
                        "SELECT a FROM Artist a WHERE LOWER(a.name) LIKE LOWER(:keyword) ORDER BY a.name ASC",
                        Artist.class
                )
                .setParameter("keyword", keyword)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Song> searchSongs(String keyword, int limit) {
        return entityManager.createQuery(
                        "SELECT s FROM Song s " +
                                "JOIN FETCH s.artist " +
                                "JOIN FETCH s.genre " +
                                "WHERE LOWER(s.title) LIKE LOWER(:keyword) " +
                                "OR LOWER(s.lyrics) LIKE LOWER(:keyword) " +
                                "OR LOWER(s.artist.name) LIKE LOWER(:keyword) " +
                                "ORDER BY s.title ASC",
                        Song.class
                )
                .setParameter("keyword", keyword)
                .setMaxResults(limit)
                .getResultList();
    }
}
