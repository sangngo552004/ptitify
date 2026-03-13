package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.ArtistResponse;
import com.sangngo552004.musicapp.dto.CursorResult;
import com.sangngo552004.musicapp.dto.GenreResponse;
import com.sangngo552004.musicapp.dto.SongResponse;
import com.sangngo552004.musicapp.entity.Song;
import com.sangngo552004.musicapp.exception.ResourceNotFoundException;
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
    private MusicMapper musicMapper;

    public List<GenreResponse> getGenres() {
        return genreRepository.findAll()
                .stream()
                .map(musicMapper::toGenreResponse)
                .toList();
    }

    public List<ArtistResponse> getArtists() {
        return artistRepository.findAll()
                .stream()
                .map(musicMapper::toArtistResponse)
                .toList();
    }

    public CursorResult<SongResponse> getSongsByGenre(Long genreId, Long cursor, Integer limit) {
        genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre was not found"));

        List<Song> songs = songRepository.findByGenreWithCursor(genreId, normalizeCursor(cursor), normalizeLimit(limit));
        List<SongResponse> items = songs.stream()
                .map(musicMapper::toSongResponse)
                .toList();

        return new CursorResult<>(items, songs.isEmpty() ? null : songs.get(songs.size() - 1).getId());
    }

    public CursorResult<SongResponse> getSongsByArtist(Long artistId, Long cursor, Integer limit) {
        artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist was not found"));

        List<Song> songs = songRepository.findByArtistWithCursor(artistId, normalizeCursor(cursor), normalizeLimit(limit));
        List<SongResponse> items = songs.stream()
                .map(musicMapper::toSongResponse)
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
