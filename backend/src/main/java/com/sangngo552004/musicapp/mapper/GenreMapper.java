package com.sangngo552004.musicapp.mapper;

import com.sangngo552004.musicapp.dto.response.GenreResponse;
import com.sangngo552004.musicapp.entity.Genre;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenreMapper {

    public GenreResponse toResponse(Genre genre) {
        GenreResponse response = new GenreResponse();
        response.setId(genre.getId());
        response.setName(genre.getName());
        return response;
    }
}
