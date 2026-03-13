package com.sangngo552004.musicapp.repository;

import com.sangngo552004.musicapp.entity.Genre;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class GenreRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<Genre> findAll() {
        return entityManager.createQuery("SELECT g FROM Genre g ORDER BY g.name ASC", Genre.class)
                .getResultList();
    }

    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }
}
