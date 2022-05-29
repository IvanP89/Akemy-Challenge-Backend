package com.ivan.alkemybackendchallenge.feature.dto.data;

public class ReducedMediaCharacterDto {
    private String name;
    private String image;

    public ReducedMediaCharacterDto(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public ReducedMediaCharacterDto() {
    }

    public String getName() {
        return name;
    }

    public ReducedMediaCharacterDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ReducedMediaCharacterDto setImage(String image) {
        this.image = image;
        return this;
    }
}
