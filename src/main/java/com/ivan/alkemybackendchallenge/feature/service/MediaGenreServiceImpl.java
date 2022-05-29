package com.ivan.alkemybackendchallenge.feature.service;

import com.ivan.alkemybackendchallenge.feature.domain.MediaGenre;
import com.ivan.alkemybackendchallenge.feature.dto.converter.DomainDtoMapper;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaGenreDto;
import com.ivan.alkemybackendchallenge.feature.repository.MediaGenreRepository;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;
import com.ivan.alkemybackendchallenge.security.utility.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MediaGenreServiceImpl implements MediaGenreService {

    private final MediaGenreRepository mediaGenreRepository;
    private final DomainDtoMapper dtoMapper;
    private final EntityValidator<MediaGenre> mediaGenreValidator;

    @Autowired
    public MediaGenreServiceImpl(MediaGenreRepository mediaGenreRepository, DomainDtoMapper dtoMapper, EntityValidator<MediaGenre> mediaGenreValidator) {
        this.mediaGenreRepository = mediaGenreRepository;
        this.dtoMapper = dtoMapper;
        this.mediaGenreValidator = mediaGenreValidator;
    }

    @Override
    public MediaGenreDto createGenre(MediaGenreDto dto) throws CustomIllegalArgumentException, EntityAlreadyExistsException {
        MediaGenre mediaGenreEntity = this.dtoMapper.genreDtoToEntity(dto);
        this.mediaGenreValidator.validateForCreation(mediaGenreEntity); // throws exception if validation fails.
        mediaGenreEntity = this.mediaGenreRepository.save(mediaGenreEntity);
        return this.dtoMapper.genreToDto(mediaGenreEntity);
    }

    @Override
    public MediaGenreDto getMediaGenre(Long id) throws EntityIdentifierNotFoundException {
        if (id == null) {
            throw new CustomIllegalArgumentException("Genre id must be provided");
        }
        MediaGenre mediaGenreEntity = this.getMediaGenreEntity(id);
        return this.dtoMapper.genreToDto(mediaGenreEntity);
    }

    @Override
    public MediaGenre getMediaGenreEntity(Long id) throws EntityIdentifierNotFoundException {
        return this.mediaGenreRepository.findById(id)
                .orElseThrow( () -> new EntityIdentifierNotFoundException("Media genre with id=" + id + " not found") );
    }

    @Override
    public MediaGenreDto getMediaGenre(String name) throws EntityIdentifierNotFoundException {
        if (name == null) {
            throw new CustomIllegalArgumentException("Genre name must be provided");
        }
        MediaGenre mediaGenreEntity = this.mediaGenreRepository.findByNameIgnoreCase(name)
                .orElseThrow( () -> new EntityIdentifierNotFoundException("Media genre with name=" + name + " not found") );
        return this.dtoMapper.genreToDto(mediaGenreEntity);
    }

}
