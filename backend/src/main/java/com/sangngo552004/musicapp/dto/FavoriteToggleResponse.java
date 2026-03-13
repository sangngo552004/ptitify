package com.sangngo552004.musicapp.dto;

public class FavoriteToggleResponse {

    private boolean favorited;
    private SongResponse song;

    public FavoriteToggleResponse() {
    }

    public FavoriteToggleResponse(boolean favorited, SongResponse song) {
        this.favorited = favorited;
        this.song = song;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public SongResponse getSong() {
        return song;
    }

    public void setSong(SongResponse song) {
        this.song = song;
    }
}
