package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.response.ArtistResponse;
import com.sangngo552004.musicapp.dto.response.CursorResult;
import com.sangngo552004.musicapp.dto.response.GenreResponse;
import com.sangngo552004.musicapp.dto.response.SongResponse;
import com.sangngo552004.musicapp.entity.Song;
import com.sangngo552004.musicapp.exception.ResourceNotFoundException;
import com.sangngo552004.musicapp.mapper.ArtistMapper;
import com.sangngo552004.musicapp.mapper.GenreMapper;
import com.sangngo552004.musicapp.mapper.SongMapper;
import com.sangngo552004.musicapp.repository.ArtistRepository;
import com.sangngo552004.musicapp.repository.GenreRepository;
import com.sangngo552004.musicapp.repository.SongRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class MusicCatalogService {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 100;

    @Inject
    private GenreRepository genreRepository;

    @Inject
    private ArtistRepository artistRepository;

    @Inject
    private SongRepository songRepository;

    @Inject
    private GenreMapper genreMapper;

    @Inject
    private ArtistMapper artistMapper;

    @Inject
    private SongMapper songMapper;

    public List<GenreResponse> getGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toResponse)
                .toList();
    }

    public List<ArtistResponse> getArtists() {
        return artistRepository.findAll()
                .stream()
                .map(artistMapper::toResponse)
                .toList();
    }

    public CursorResult<SongResponse> getSongsByGenre(Long genreId, Long cursor, Integer limit) {
        genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre was not found"));

        List<Song> songs = songRepository.findByGenreWithCursor(genreId, normalizeCursor(cursor), normalizeLimit(limit));
        List<SongResponse> items = songs.stream()
                .map(songMapper::toResponse)
                .toList();

        return new CursorResult<>(items, songs.isEmpty() ? null : songs.get(songs.size() - 1).getId());
    }

    public CursorResult<SongResponse> getSongsByArtist(Long artistId, Long cursor, Integer limit) {
        artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist was not found"));

        List<Song> songs = songRepository.findByArtistWithCursor(artistId, normalizeCursor(cursor), normalizeLimit(limit));
        List<SongResponse> items = songs.stream()
                .map(songMapper::toResponse)
                .toList();

        return new CursorResult<>(items, songs.isEmpty() ? null : songs.get(songs.size() - 1).getId());
    }

    private Long normalizeCursor(Long cursor) {
        return cursor == null || cursor <= 0 ? null : cursor;
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}
