package com.ivan.alkemybackendchallenge.feature.dto.converter;

import com.ivan.alkemybackendchallenge.feature.domain.MediaWork;
import com.ivan.alkemybackendchallenge.feature.domain.MediaWorkType;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaWorkDto;
import com.ivan.alkemybackendchallenge.security.dto.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class MediaWorkDtoConverter extends Converter<MediaWorkDto, MediaWork> {

    public MediaWorkDtoConverter() {
        super(MediaWorkDtoConverter::convertFromDto, MediaWorkDtoConverter::convertFromEntity);
    }

    /**
     * Converts everything except the mediaCharacters and mediaGenres Collections, which are set as empty.
     *
     * @param dto   all the entity data including its mediaCharacters and mediaGenres Collections (to be converted elsewhere).
     * @return  the entity without its mediaCharacters and mediaGenres Collections (to be added somewhere else).
     */
    private static MediaWork convertFromDto(MediaWorkDto dto) {
        if (dto == null) {
            return null;
        }
        MediaWorkType mediaWorkType;
        try {
            mediaWorkType = MediaWorkType.valueOf( dto.getMediaWorkType().toUpperCase() );
        } catch (Exception e) {
            mediaWorkType = null; // Handled by EntityValidator afterwards.
        }
        LocalDate releaseDate = null;
        if (dto.getReleaseDate() != null) {
            try {
                releaseDate = LocalDate.parse( dto.getReleaseDate() );
            } catch (DateTimeParseException e) {
                releaseDate = LocalDate.MIN; // Handled by EntityValidator afterwards.
            }
        }
        if (dto.getTitle() != null) {
            dto.setTitle( dto.getTitle().toLowerCase() );
        }
        return new MediaWork(
                dto.getId(),
                mediaWorkType,
                dto.getImageUrl(),
                dto.getTitle(),
                releaseDate,
                dto.getScore()
        );
    } // end of convertFromDto()

    /**
     * Converts everything except the mediaCharacters and mediaGenres Collections, which are set as empty.
     *
     * @param mediaWork the complete entity
     * @return  the DTO without its mediaCharacters and mediaGenres Collections (to be added somewhere else).
     */
    private static MediaWorkDto convertFromEntity(MediaWork mediaWork) {
        if (mediaWork == null) {
            return null;
        }
        return new MediaWorkDto(
                mediaWork.getId(),
                mediaWork.getMediaWorkType().name().toLowerCase(),
                mediaWork.getImageUrl(),
                mediaWork.getTitle(),
                mediaWork.getReleaseDate().toString(),
                mediaWork.getScore()
        );
    } // end of convertFromEntity()

}
