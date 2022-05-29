package com.ivan.alkemybackendchallenge.feature.dto.data;

public class ReducedMediaWorkDto {

    private String imageUrl;
    private String title;
    private String releaseDate;

    public ReducedMediaWorkDto() {

    }

    public ReducedMediaWorkDto(String imageUrl, String title, String releaseDate) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ReducedMediaWorkDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ReducedMediaWorkDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public ReducedMediaWorkDto setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

}
