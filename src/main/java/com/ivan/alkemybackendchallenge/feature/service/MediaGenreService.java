package com.ivan.alkemybackendchallenge.feature.service;

import com.ivan.alkemybackendchallenge.feature.domain.MediaGenre;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaGenreDto;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;

public interface MediaGenreService {

    MediaGenreDto createGenre(MediaGenreDto dto) throws CustomIllegalArgumentException, EntityAlreadyExistsException;
    MediaGenreDto getMediaGenre(Long id) throws EntityIdentifierNotFoundException;
    MediaGenre getMediaGenreEntity(Long id) throws EntityIdentifierNotFoundException;
    MediaGenreDto getMediaGenre(String name) throws EntityIdentifierNotFoundException;

}
