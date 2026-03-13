package com.sangngo552004.musicapp.mapper;

import com.sangngo552004.musicapp.dto.response.SongResponse;
import com.sangngo552004.musicapp.entity.Song;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SongMapper {

    @Inject
    private ArtistMapper artistMapper;

    @Inject
    private GenreMapper genreMapper;

    public SongResponse toResponse(Song song) {
        SongResponse response = new SongResponse();
        response.setId(song.getId());
        response.setTitle(song.getTitle());
        response.setLyrics(song.getLyrics());
        response.setDuration(song.getDuration());
        response.setFileUrl(song.getFileUrl());
        response.setPlayCount(song.getPlayCount());
        response.setArtist(artistMapper.toResponse(song.getArtist()));
        response.setGenre(genreMapper.toResponse(song.getGenre()));
        return response;
    }
}
