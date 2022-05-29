package com.ivan.alkemybackendchallenge.feature.dto.data;

import java.util.ArrayList;
import java.util.Collection;

public class MediaGenreDto {

    private Long id;
    private String name;
    private String imageUrl;
    private Collection<MediaWorkDto> mediaWorks = new ArrayList<>();

    public MediaGenreDto() {
    }

    public MediaGenreDto(Long id, String name, String imageUrl, Collection<MediaWorkDto> mediaWorks) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.mediaWorks = mediaWorks;
    }

    public MediaGenreDto(String name, String imageUrl, Collection<MediaWorkDto> mediaWorks) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.mediaWorks = mediaWorks;
    }

    public MediaGenreDto(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public MediaGenreDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MediaGenreDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MediaGenreDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Collection<MediaWorkDto> getMediaWorks() {
        return mediaWorks;
    }

    public MediaGenreDto setMediaWorks(Collection<MediaWorkDto> mediaWorks) {
        this.mediaWorks = mediaWorks;
        return this;
    }

}
