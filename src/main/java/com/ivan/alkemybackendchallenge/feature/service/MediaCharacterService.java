package com.ivan.alkemybackendchallenge.feature.service;

import com.ivan.alkemybackendchallenge.feature.domain.MediaCharacter;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaCharacterDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.ReducedMediaCharacterDto;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;

import java.util.List;

public interface MediaCharacterService {

    MediaCharacterDto createMediaCharacter(MediaCharacterDto dto) throws CustomIllegalArgumentException, EntityAlreadyExistsException;
    MediaCharacterDto updateMediaCharacter(MediaCharacterDto dto) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException;
    List<ReducedMediaCharacterDto> getMediaCharacters(String name, Integer age, Long mediaWorkId);
    MediaCharacter getMediaCharacterEntity(Long id) throws EntityIdentifierNotFoundException;
    void deleteMediaCharacter(Long id) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException;

}
