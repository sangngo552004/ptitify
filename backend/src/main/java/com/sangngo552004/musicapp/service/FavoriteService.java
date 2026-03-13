package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.CursorResult;
import com.sangngo552004.musicapp.dto.FavoriteToggleResponse;
import com.sangngo552004.musicapp.dto.SongResponse;
import com.sangngo552004.musicapp.entity.FavoriteSong;
import com.sangngo552004.musicapp.entity.Song;
import com.sangngo552004.musicapp.entity.User;
import com.sangngo552004.musicapp.exception.ResourceNotFoundException;
import com.sangngo552004.musicapp.repository.FavoriteSongRepository;
import com.sangngo552004.musicapp.repository.SongRepository;
import com.sangngo552004.musicapp.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class FavoriteService {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 100;

    @Inject
    private FavoriteSongRepository favoriteSongRepository;

    @Inject
    private SongRepository songRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MusicMapper musicMapper;

    public FavoriteToggleResponse toggleFavorite(Long userId, Long songId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found"));
        Song song = songRepository.findByIdWithRelations(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song was not found"));

        return favoriteSongRepository.findByUserIdAndSongId(userId, songId)
                .map(existingFavorite -> {
                    favoriteSongRepository.delete(existingFavorite);
                    return new FavoriteToggleResponse(false, musicMapper.toSongResponse(existingFavorite.getSong()));
                })
                .orElseGet(() -> {
                    FavoriteSong favoriteSong = new FavoriteSong();
                    favoriteSong.setUser(user);
                    favoriteSong.setSong(song);
                    favoriteSongRepository.save(favoriteSong);
                    return new FavoriteToggleResponse(true, musicMapper.toSongResponse(song));
                });
    }

    public CursorResult<SongResponse> getFavoriteSongs(Long userId, Long cursor, Integer limit) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found"));

        List<FavoriteSong> favorites = favoriteSongRepository.findByUserIdWithCursor(
                userId,
                normalizeCursor(cursor),
                normalizeLimit(limit)
        );

        List<SongResponse> items = favorites.stream()
                .map(FavoriteSong::getSong)
                .map(musicMapper::toSongResponse)
                .toList();

        return new CursorResult<>(items, favorites.isEmpty() ? null : favorites.get(favorites.size() - 1).getId());
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
