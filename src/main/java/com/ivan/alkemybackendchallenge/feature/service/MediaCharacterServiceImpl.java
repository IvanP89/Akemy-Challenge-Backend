package com.ivan.alkemybackendchallenge.feature.service;

import com.ivan.alkemybackendchallenge.feature.domain.MediaCharacter;
import com.ivan.alkemybackendchallenge.feature.dto.converter.DomainDtoMapper;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaCharacterDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.ReducedMediaCharacterDto;
import com.ivan.alkemybackendchallenge.feature.repository.MediaCharacterRepository;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;
import com.ivan.alkemybackendchallenge.security.utility.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MediaCharacterServiceImpl implements MediaCharacterService {

    private final MediaCharacterRepository mediaCharacterRepository;
    private final DomainDtoMapper dtoMapper;
    private final EntityValidator<MediaCharacter> mediaCharacterValidator;

    @Autowired
    public MediaCharacterServiceImpl(MediaCharacterRepository mediaCharacterRepository,
                                     DomainDtoMapper dtoMapper,
                                     EntityValidator<MediaCharacter> mediaCharacterValidator) {

        this.mediaCharacterRepository = mediaCharacterRepository;
        this.dtoMapper = dtoMapper;
        this.mediaCharacterValidator = mediaCharacterValidator;
    }

    @Override
    public MediaCharacterDto createMediaCharacter(MediaCharacterDto dto) throws CustomIllegalArgumentException, EntityAlreadyExistsException {
        MediaCharacter mediaCharacterEntity = this.dtoMapper.characterDtoToEntity(dto);
        this.mediaCharacterValidator.validateForCreation(mediaCharacterEntity); // throws exception if validation fails.
        mediaCharacterEntity = this.mediaCharacterRepository.save(mediaCharacterEntity);
        return this.dtoMapper.characterToDto(mediaCharacterEntity);
    }

    @Override
    public MediaCharacterDto updateMediaCharacter(MediaCharacterDto dto) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException {
        if (dto.getId() == null) {
            throw new CustomIllegalArgumentException("Character id must be provided");
        }
        MediaCharacter oldVersionEntity = this.getMediaCharacterEntity( dto.getId() );
        MediaCharacter updatedVersionEntity = this.dtoMapper.characterDtoToEntity(dto);
        this.mediaCharacterValidator.validateForUpdate(updatedVersionEntity); // throws exception if validation fails.
        oldVersionEntity.setImageUrl( updatedVersionEntity.getImageUrl() )
                .setName( updatedVersionEntity.getName() )
                .setAge( updatedVersionEntity.getAge() )
                .setBodyWeight( updatedVersionEntity.getBodyWeight() )
                .setHistory( updatedVersionEntity.getHistory() );
        // Since the class is annotated as @Transactional, the changes are automatically saved.
        return this.dtoMapper.characterToDto(oldVersionEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReducedMediaCharacterDto> getMediaCharacters(String name, Integer age, Long mediaWorkId) {
        if (name != null) {
            name = name.toLowerCase();
        }
        List<MediaCharacter> mediaCharacterEntities = this.mediaCharacterRepository
                .findByNameIgnoreCaseAndAgeAndMediaWorks_Id(name, age, mediaWorkId);
        if (mediaCharacterEntities == null || mediaCharacterEntities.isEmpty()) {
            return new ArrayList<>();
        }
        return mediaCharacterEntities.stream()
                .map(this.dtoMapper::characterToReducedDto)
                .collect(Collectors.toList());
    }

    @Override
    public MediaCharacter getMediaCharacterEntity(Long id) throws EntityIdentifierNotFoundException {
        return this.mediaCharacterRepository.findById(id)
                .orElseThrow( () -> new EntityIdentifierNotFoundException("Character not found") );
    }

    @Override
    public void deleteMediaCharacter(Long id) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException {
        if (id == null) {
            throw new CustomIllegalArgumentException("Character id must be provided");
        }
        MediaCharacter mediaCharacter = this.getMediaCharacterEntity(id);
        if (mediaCharacter.getMediaWorks() == null || mediaCharacter.getMediaWorks().isEmpty()) {
            this.mediaCharacterRepository.deleteById(id);
        } else {
            throw new CustomIllegalArgumentException("Cannot delete a character with associated media works");
        }
    }

}
