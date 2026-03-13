package com.sangngo552004.musicapp.repository;

import com.sangngo552004.musicapp.entity.Artist;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class ArtistRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<Artist> findAll() {
        return entityManager.createQuery("SELECT a FROM Artist a ORDER BY a.name ASC", Artist.class)
                .getResultList();
    }

    public Optional<Artist> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Artist.class, id));
    }
}
