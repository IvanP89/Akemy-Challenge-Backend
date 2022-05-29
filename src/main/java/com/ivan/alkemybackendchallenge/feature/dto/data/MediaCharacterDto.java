package com.ivan.alkemybackendchallenge.feature.dto.data;

import java.util.ArrayList;
import java.util.Collection;

public class MediaCharacterDto {

    private Long id;
    private String imageUrl;
    private String name;
    private Integer age;
    private Double bodyWeight;
    private String history;
    private Collection<MediaWorkDto> mediaWorks = new ArrayList<>();

    public MediaCharacterDto() {

    }

    public MediaCharacterDto(Long id, String imageUrl, String name, Integer age, Double bodyWeight, String history, Collection<MediaWorkDto> mediaWorks) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.age = age;
        this.bodyWeight = bodyWeight;
        this.history = history;
        this.mediaWorks = mediaWorks;
    }

    public MediaCharacterDto(String imageUrl, String name, Integer age, Double bodyWeight, String history, Collection<MediaWorkDto> mediaWorks) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.age = age;
        this.bodyWeight = bodyWeight;
        this.history = history;
        this.mediaWorks = mediaWorks;
    }

    public MediaCharacterDto(Long id, String imageUrl, String name, Integer age, Double bodyWeight, String history) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.age = age;
        this.bodyWeight = bodyWeight;
        this.history = history;
    }

    public Long getId() {
        return id;
    }

    public MediaCharacterDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MediaCharacterDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getName() {
        return name;
    }

    public MediaCharacterDto setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public MediaCharacterDto setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Double getBodyWeight() {
        return bodyWeight;
    }

    public MediaCharacterDto setBodyWeight(Double bodyWeight) {
        this.bodyWeight = bodyWeight;
        return this;
    }

    public String getHistory() {
        return history;
    }

    public MediaCharacterDto setHistory(String history) {
        this.history = history;
        return this;
    }

    public Collection<MediaWorkDto> getMediaWorks() {
        return mediaWorks;
    }

    public MediaCharacterDto setMediaWorks(Collection<MediaWorkDto> mediaWorks) {
        this.mediaWorks = mediaWorks;
        return this;
    }

}
