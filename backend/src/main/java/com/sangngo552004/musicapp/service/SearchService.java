package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.response.SearchResponse;
import com.sangngo552004.musicapp.mapper.ArtistMapper;
import com.sangngo552004.musicapp.mapper.SongMapper;
import com.sangngo552004.musicapp.repository.SearchRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class SearchService {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 100;

    @Inject
    private SearchRepository searchRepository;

    @Inject
    private SongMapper songMapper;

    @Inject
    private ArtistMapper artistMapper;

    public SearchResponse search(String query, int limit) {
        if (query == null || query.isBlank()) {
            return new SearchResponse(List.of(), List.of());
        }

        int normalizedLimit = normalizeLimit(limit);
        String keyword = "%" + query.trim() + "%";

        return new SearchResponse(
                searchRepository.searchSongs(keyword, normalizedLimit)
                        .stream()
                        .map(songMapper::toResponse)
                        .toList(),
                searchRepository.searchArtists(keyword, normalizedLimit)
                        .stream()
                        .map(artistMapper::toResponse)
                        .toList()
        );
    }

    private int normalizeLimit(int limit) {
        if (limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}
