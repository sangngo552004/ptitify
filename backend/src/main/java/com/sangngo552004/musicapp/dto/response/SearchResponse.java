package com.sangngo552004.musicapp.dto.response;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {

    private List<SongResponse> songs = new ArrayList<>();
    private List<ArtistResponse> artists = new ArrayList<>();

    public SearchResponse() {
    }

    public SearchResponse(List<SongResponse> songs, List<ArtistResponse> artists) {
        this.songs = songs;
        this.artists = artists;
    }

    public List<SongResponse> getSongs() {
        return songs;
    }

    public void setSongs(List<SongResponse> songs) {
        this.songs = songs;
    }

    public List<ArtistResponse> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistResponse> artists) {
        this.artists = artists;
    }
}
