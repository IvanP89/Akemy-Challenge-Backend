package com.ivan.alkemybackendchallenge.feature.dto.converter;

import com.ivan.alkemybackendchallenge.feature.domain.MediaCharacter;
import com.ivan.alkemybackendchallenge.feature.domain.MediaGenre;
import com.ivan.alkemybackendchallenge.feature.domain.MediaWork;
import com.ivan.alkemybackendchallenge.feature.dto.data.*;
import com.ivan.alkemybackendchallenge.security.dto.converter.DtoEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Used to convert the domain entities to DTOs and the other way around.
 *
 * Created to encapsulate the extra operations needed to apply this conversion to objects with many-to-many associations.
 * These extra operations are related to the conversion of the collections inside the objects.
 */
@Component
public class DomainDtoMapper {

    private final DtoEntityConverter<MediaCharacterDto, MediaCharacter> characterConverter;
    private final DtoEntityConverter<MediaGenreDto, MediaGenre> genreConverter;
    private final DtoEntityConverter<MediaWorkDto, MediaWork> mediaWorkConverter;

    @Autowired
    public DomainDtoMapper(DtoEntityConverter<MediaCharacterDto, MediaCharacter> characterConverter,
                           DtoEntityConverter<MediaGenreDto, MediaGenre> genreConverter,
                           DtoEntityConverter<MediaWorkDto, MediaWork> mediaWorkConverter) {

        this.characterConverter = characterConverter;
        this.genreConverter = genreConverter;
        this.mediaWorkConverter = mediaWorkConverter;
    }

    public MediaCharacter characterDtoToEntity(MediaCharacterDto dto) {
        MediaCharacter characterEntity = this.characterConverter.convertToEntity(dto);
        Set<MediaWork> mediaWorkEntities = new LinkedHashSet<>();
        if (dto.getMediaWorks() != null) {
            mediaWorkEntities = dto.getMediaWorks()
                    .stream()
                    .map(this.mediaWorkConverter::convertToEntity)
                    .collect(Collectors.toSet());
        }
        characterEntity.setMediaWorks(mediaWorkEntities);
        return characterEntity;
    }

    public MediaCharacterDto characterToDto(MediaCharacter characterEntity) {
        MediaCharacterDto dto = this.characterConverter.convertToDto(characterEntity);
        Collection<MediaWorkDto> mediaWorkDtos = new ArrayList<>();
        if (characterEntity.getMediaWorks() != null) {
            mediaWorkDtos = characterEntity.getMediaWorks()
                    .stream()
                    .map(this.mediaWorkConverter::convertToDto)
                    .collect(Collectors.toList());
        }
        dto.setMediaWorks(mediaWorkDtos);
        return dto;
    }

    public ReducedMediaCharacterDto characterToReducedDto(MediaCharacter characterEntity) {
        return new ReducedMediaCharacterDto(characterEntity.getName(), characterEntity.getImageUrl());
    }

    public MediaGenre genreDtoToEntity(MediaGenreDto dto) {
        MediaGenre genreEntity = this.genreConverter.convertToEntity(dto);
        Set<MediaWork> mediaWorkEntities = new LinkedHashSet<>();
        if (dto.getMediaWorks() != null) {
            mediaWorkEntities = dto.getMediaWorks()
                    .stream()
                    .map(this.mediaWorkConverter::convertToEntity)
                    .collect(Collectors.toSet());
        }
        genreEntity.setMediaWorks(mediaWorkEntities);
        return genreEntity;
    }

    public MediaGenreDto genreToDto(MediaGenre genreEntity) {
        MediaGenreDto dto = this.genreConverter.convertToDto(genreEntity);
        Collection<MediaWorkDto> mediaWorkDtos = new ArrayList<>();
        if (genreEntity.getMediaWorks() != null) {
            mediaWorkDtos = genreEntity.getMediaWorks()
                    .stream()
                    .map(this.mediaWorkConverter::convertToDto)
                    .collect(Collectors.toList());
        }
        dto.setMediaWorks(mediaWorkDtos);
        return dto;
    }

    public MediaWork mediaWorkDtoToEntity(MediaWorkDto dto) {
        MediaWork mediaWorkEntity = this.mediaWorkConverter.convertToEntity(dto);
        Set<MediaCharacter> mediaCharacterEntities = new LinkedHashSet<>();
        if (dto.getMediaCharacters() != null) {
            mediaCharacterEntities = dto.getMediaCharacters()
                    .stream()
                    .map(this.characterConverter::convertToEntity)
                    .collect(Collectors.toSet());
        }
        mediaWorkEntity.setMediaCharacters(mediaCharacterEntities);
        Set<MediaGenre> mediaGenreEntities = new LinkedHashSet<>();
        if (dto.getMediaGenres() != null) {
            mediaGenreEntities = dto.getMediaGenres()
                    .stream()
                    .map(this.genreConverter::convertToEntity)
                    .collect(Collectors.toSet());
        }
        mediaWorkEntity.setMediaGenres(mediaGenreEntities);
        return mediaWorkEntity;
    }

    public MediaWorkDto mediaWorkToDto(MediaWork mediaWorkEntity) {
        MediaWorkDto dto = this.mediaWorkConverter.convertToDto(mediaWorkEntity);
        Collection<MediaCharacterDto> mediaCharacterDtos = new ArrayList<>();
        if (mediaWorkEntity.getMediaCharacters() != null) {
            mediaCharacterDtos = mediaWorkEntity.getMediaCharacters()
                    .stream()
                    .map(this.characterConverter::convertToDto)
                    .collect(Collectors.toList());
        }
        dto.setMediaCharacters(mediaCharacterDtos);
        Collection<MediaGenreDto> mediaGenreDtos = new ArrayList<>();
        if (mediaWorkEntity.getMediaGenres() != null) {
            mediaGenreDtos = mediaWorkEntity.getMediaGenres()
                    .stream()
                    .map(this.genreConverter::convertToDto)
                    .collect(Collectors.toList());
        }
        dto.setMediaGenres(mediaGenreDtos);
        return dto;
    }

    public ReducedMediaWorkDto mediaWorkToReducedDto(MediaWork mediaWorkEntity) {
        return new ReducedMediaWorkDto(
                mediaWorkEntity.getImageUrl(),
                mediaWorkEntity.getTitle(),
                mediaWorkEntity.getReleaseDate().toString()
        );
    }

}
