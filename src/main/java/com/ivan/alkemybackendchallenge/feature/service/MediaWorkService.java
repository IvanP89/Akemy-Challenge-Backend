package com.ivan.alkemybackendchallenge.feature.service;

import com.ivan.alkemybackendchallenge.feature.dto.data.MediaWorkDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.ReducedMediaWorkDto;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;

import java.util.List;

public interface MediaWorkService {

    MediaWorkDto createMediaWork(MediaWorkDto dto) throws CustomIllegalArgumentException, EntityAlreadyExistsException, EntityIdentifierNotFoundException;
    MediaWorkDto updateMediaWork(MediaWorkDto dto) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException;
    MediaWorkDto getMediaWork(Long id) throws EntityIdentifierNotFoundException;
    List<ReducedMediaWorkDto> getMediaWork(String title, Long genreId, String order) throws CustomIllegalArgumentException;
    void deleteMediaWork(Long id) throws EntityIdentifierNotFoundException;
    void addCharacterToMediaWork(Long idCharacter, Long idMediaWork) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException;
    void removeCharacterFromMediaWork(Long idCharacter, Long idMediaWork) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException;
    void addGenreToMediaWork(Long idGenre, Long idMediaWork) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException;
    void removeGenreFromMediaWork(Long idGenre, Long idMediaWork) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException;

}
