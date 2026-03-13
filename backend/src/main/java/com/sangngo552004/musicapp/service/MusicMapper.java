package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.ArtistResponse;
import com.sangngo552004.musicapp.dto.GenreResponse;
import com.sangngo552004.musicapp.dto.SongResponse;
import com.sangngo552004.musicapp.entity.Artist;
import com.sangngo552004.musicapp.entity.Genre;
import com.sangngo552004.musicapp.entity.Song;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MusicMapper {

    public GenreResponse toGenreResponse(Genre genre) {
        GenreResponse response = new GenreResponse();
        response.setId(genre.getId());
        response.setName(genre.getName());
        return response;
    }

    public ArtistResponse toArtistResponse(Artist artist) {
        ArtistResponse response = new ArtistResponse();
        response.setId(artist.getId());
        response.setName(artist.getName());
        response.setBio(artist.getBio());
        response.setAvatarUrl(artist.getAvatarUrl());
        return response;
    }

    public SongResponse toSongResponse(Song song) {
        SongResponse response = new SongResponse();
        response.setId(song.getId());
        response.setTitle(song.getTitle());
        response.setDuration(song.getDuration());
        response.setFileUrl(song.getFileUrl());
        response.setPlayCount(song.getPlayCount());
        response.setArtist(toArtistResponse(song.getArtist()));
        response.setGenre(toGenreResponse(song.getGenre()));
        return response;
    }
}
