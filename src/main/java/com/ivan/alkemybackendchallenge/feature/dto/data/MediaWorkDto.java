package com.ivan.alkemybackendchallenge.feature.dto.data;

import java.util.ArrayList;
import java.util.Collection;

public class MediaWorkDto {

    private Long id;
    private String mediaWorkType;
    private String imageUrl;
    private String title;
    private String releaseDate;
    private Double score;
    private Collection<MediaCharacterDto> mediaCharacters = new ArrayList<>();
    private Collection<MediaGenreDto> mediaGenres = new ArrayList<>();

    public MediaWorkDto() {

    }

    public MediaWorkDto(Long id, String mediaWorkType, String imageUrl, String title, String releaseDate,
                        Double score, Collection<MediaCharacterDto> mediaCharacters,
                        Collection<MediaGenreDto> mediaGenres) {

        this.id = id;
        this.mediaWorkType = mediaWorkType;
        this.imageUrl = imageUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.score = score;
        this.mediaCharacters = mediaCharacters;
        this.mediaGenres = mediaGenres;
    }

    public MediaWorkDto(String mediaWorkType, String imageUrl, String title, String releaseDate, Double score,
                        Collection<MediaCharacterDto> mediaCharacters, Collection<MediaGenreDto> mediaGenres) {

        this.mediaWorkType = mediaWorkType;
        this.imageUrl = imageUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.score = score;
        this.mediaCharacters = mediaCharacters;
        this.mediaGenres = mediaGenres;
    }

    public MediaWorkDto(Long id, String mediaWorkType, String imageUrl, String title, String releaseDate, Double score) {
        this.id = id;
        this.mediaWorkType = mediaWorkType;
        this.imageUrl = imageUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public MediaWorkDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getMediaWorkType() {
        return mediaWorkType;
    }

    public MediaWorkDto setMediaWorkType(String mediaWorkType) {
        this.mediaWorkType = mediaWorkType;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MediaWorkDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MediaWorkDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public MediaWorkDto setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Double getScore() {
        return score;
    }

    public MediaWorkDto setScore(Double score) {
        this.score = score;
        return this;
    }

    public Collection<MediaCharacterDto> getMediaCharacters() {
        return mediaCharacters;
    }

    public MediaWorkDto setMediaCharacters(Collection<MediaCharacterDto> mediaCharacters) {
        this.mediaCharacters = mediaCharacters;
        return this;
    }

    public Collection<MediaGenreDto> getMediaGenres() {
        return mediaGenres;
    }

    public MediaWorkDto setMediaGenres(Collection<MediaGenreDto> mediaGenres) {
        this.mediaGenres = mediaGenres;
        return this;
    }

}
