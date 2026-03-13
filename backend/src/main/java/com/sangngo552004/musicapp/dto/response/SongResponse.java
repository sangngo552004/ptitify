package com.sangngo552004.musicapp.dto.response;

public class SongResponse {

    private Long id;
    private String title;
    private String lyrics;
    private Integer duration;
    private String fileUrl;
    private Integer playCount;
    private ArtistResponse artist;
    private GenreResponse genre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public ArtistResponse getArtist() {
        return artist;
    }

    public void setArtist(ArtistResponse artist) {
        this.artist = artist;
    }

    public GenreResponse getGenre() {
        return genre;
    }

    public void setGenre(GenreResponse genre) {
        this.genre = genre;
    }
}
