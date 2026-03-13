package com.sangngo552004.musicapp.mapper;

import com.sangngo552004.musicapp.dto.response.ArtistResponse;
import com.sangngo552004.musicapp.entity.Artist;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArtistMapper {

    public ArtistResponse toResponse(Artist artist) {
        ArtistResponse response = new ArtistResponse();
        response.setId(artist.getId());
        response.setName(artist.getName());
        response.setBio(artist.getBio());
        response.setAvatarUrl(artist.getAvatarUrl());
        return response;
    }
}
