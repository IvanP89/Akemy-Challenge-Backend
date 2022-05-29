package com.ivan.alkemybackendchallenge.feature.dto.converter;

import com.ivan.alkemybackendchallenge.feature.domain.MediaGenre;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaGenreDto;
import com.ivan.alkemybackendchallenge.security.dto.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MediaGenreDtoConverter extends Converter<MediaGenreDto, MediaGenre> {

    public MediaGenreDtoConverter() {
        super(MediaGenreDtoConverter::convertFromDto, MediaGenreDtoConverter::convertFromEntity);
    }

    /**
     * Converts everything except the mediaWork Collection, which is set as empty.
     *
     * @param dto   all the entity data including its mediaWork Collection (to be converted elsewhere).
     * @return  the entity without its mediaWork Collection (to be added somewhere else).
     */
    private static MediaGenre convertFromDto(MediaGenreDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getName() != null) {
            dto.setName( dto.getName().toLowerCase() );
        }
        return new MediaGenre(dto.getId(), dto.getName(), dto.getImageUrl());
    }

    /**
     * Converts everything except the mediaWork Collection, which is set as empty.
     *
     * @param mediaGenre    the complete entity
     * @return  the DTO without its mediaWork Collection (to be added somewhere else).
     */
    private static MediaGenreDto convertFromEntity(MediaGenre mediaGenre) {
        if (mediaGenre == null) {
            return null;
        }
        return new MediaGenreDto(mediaGenre.getId(), mediaGenre.getName(), mediaGenre.getImageUrl());
    }

}
