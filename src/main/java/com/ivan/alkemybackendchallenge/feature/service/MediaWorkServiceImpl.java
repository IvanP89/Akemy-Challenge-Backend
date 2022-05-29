package com.ivan.alkemybackendchallenge.feature.service;

import com.ivan.alkemybackendchallenge.feature.domain.MediaCharacter;
import com.ivan.alkemybackendchallenge.feature.domain.MediaGenre;
import com.ivan.alkemybackendchallenge.feature.domain.MediaWork;
import com.ivan.alkemybackendchallenge.feature.dto.converter.DomainDtoMapper;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaWorkDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.ReducedMediaWorkDto;
import com.ivan.alkemybackendchallenge.feature.repository.MediaWorkRepository;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;
import com.ivan.alkemybackendchallenge.security.utility.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MediaWorkServiceImpl implements MediaWorkService {

    private final MediaWorkRepository mediaWorkRepository;
    private final MediaCharacterService mediaCharacterService;
    private final MediaGenreService mediaGenreService;
    private final DomainDtoMapper dtoMapper;
    private final EntityValidator<MediaWork> mediaWorkValidator;

    @Autowired
    public MediaWorkServiceImpl(MediaWorkRepository mediaWorkRepository,
                                MediaCharacterService mediaCharacterService,
                                MediaGenreService mediaGenreService,
                                DomainDtoMapper dtoMapper,
                                EntityValidator<MediaWork> mediaWorkValidator) {

        this.mediaWorkRepository = mediaWorkRepository;
        this.mediaCharacterService = mediaCharacterService;
        this.mediaGenreService = mediaGenreService;
        this.dtoMapper = dtoMapper;
        this.mediaWorkValidator = mediaWorkValidator;
    }

    @Override
    public MediaWorkDto createMediaWork(MediaWorkDto dto) throws CustomIllegalArgumentException, EntityAlreadyExistsException, EntityIdentifierNotFoundException {
        MediaWork mediaWorkEntity = this.dtoMapper.mediaWorkDtoToEntity(dto);
        this.mediaWorkValidator.validateForCreation(mediaWorkEntity); // throws exception if validation fails.
        this.setJpaTrackedCharacters(mediaWorkEntity);
        this.setJpaTrackedGenres(mediaWorkEntity);
        mediaWorkEntity = this.mediaWorkRepository.save(mediaWorkEntity);
        return this.dtoMapper.mediaWorkToDto(mediaWorkEntity);
    }

    private MediaWork getMediaWorkEntity(Long id) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException {
        if (id == null) {
            throw new CustomIllegalArgumentException("Media work id must be provided");
        }
        return this.mediaWorkRepository.findById(id)
                .orElseThrow( () -> new EntityIdentifierNotFoundException("Media work not found"));
    }

    @Override
    public MediaWorkDto updateMediaWork(MediaWorkDto dto) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException {
        if (dto.getId() == null) {
            throw new CustomIllegalArgumentException("Media work id must be provided");
        }
        MediaWork oldVersionEntity = this.getMediaWorkEntity(dto.getId());
        MediaWork updatedVersionEntity = this.dtoMapper.mediaWorkDtoToEntity(dto);
        this.mediaWorkValidator.validateForUpdate(updatedVersionEntity); // throws exception if validation fails.
        oldVersionEntity.setMediaWorkType( updatedVersionEntity.getMediaWorkType() )
                .setTitle( updatedVersionEntity.getTitle() )
                .setImageUrl( updatedVersionEntity.getImageUrl() )
                .setReleaseDate( updatedVersionEntity.getReleaseDate() )
                .setScore( updatedVersionEntity.getScore() );
        // Since the class is annotated as @Transactional, the changes are automatically saved.
        return this.dtoMapper.mediaWorkToDto(oldVersionEntity);
    }

    @Override
    public MediaWorkDto getMediaWork(Long id) throws EntityIdentifierNotFoundException {
        if (id == null) {
            throw new CustomIllegalArgumentException("Media work id must be provided");
        }
        MediaWork mediaWork = this.mediaWorkRepository.findById(id)
                .orElseThrow( () -> new EntityIdentifierNotFoundException("Media work with id=" + id + " not found") );
        return this.dtoMapper.mediaWorkToDto(mediaWork);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReducedMediaWorkDto> getMediaWork(String title, Long genreId, String order) throws CustomIllegalArgumentException {

        if (order != null && !order.toUpperCase().equals("DESC") && !order.toUpperCase().equals("ASC")) {
            throw new CustomIllegalArgumentException("Result order param must be asc or desc");
        }
        if (title != null) {
            title = title.toLowerCase();
        }

        Sort sort;
        if (order != null) {
            if (order.toUpperCase().equals("DESC")) {
                sort = Sort.by("title").descending();
            } else {
                sort = Sort.by("title").ascending();
            }
        } else {
            sort = Sort.unsorted();
        }

        List<MediaWork> mediaWorkEntities = this.mediaWorkRepository.findByTitleIgnoreCaseAndMediaGenres_Id(title, genreId, sort);

        if (mediaWorkEntities == null || mediaWorkEntities.isEmpty()) {
            return new ArrayList<>();
        }
        return mediaWorkEntities.stream()
                .map(this.dtoMapper::mediaWorkToReducedDto)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteMediaWork(Long id) throws EntityIdentifierNotFoundException {
        if (id == null) {
            throw new CustomIllegalArgumentException("Media work id must be provided");
        }
        try {
            this.mediaWorkRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityIdentifierNotFoundException("Media work not found", e);
        }
    }

    @Override
    public void addCharacterToMediaWork(Long idCharacter, Long idMediaWork) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException {
        if (idCharacter == null) {
            throw new CustomIllegalArgumentException("Character id must be provided");
        }
        if (idMediaWork == null) {
            throw new CustomIllegalArgumentException("Media work id must be provided");
        }
        MediaWork mediaWork = this.getMediaWorkEntity(idMediaWork);
        Boolean characterAlreadyAssociated = mediaWork.getMediaCharacters()
                .stream()
                .anyMatch( mediaCharacter -> mediaCharacter.getId().equals(idCharacter) );
        if (characterAlreadyAssociated) {
            throw new CustomIllegalArgumentException("The character is already associated with the media work");
        }
        MediaCharacter mediaCharacter = this.mediaCharacterService.getMediaCharacterEntity(idCharacter); // throws exception if not found.
        mediaWork.addMediaCharacter(mediaCharacter);
        // Because the class is annotated as transactional, changes are saved automatically.
    }

    @Override
    public void removeCharacterFromMediaWork(Long idCharacter, Long idMediaWork) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException {
        if (idCharacter == null) {
            throw new CustomIllegalArgumentException("Character id must be provided");
        }
        if (idMediaWork == null) {
            throw new CustomIllegalArgumentException("Media work id must be provided");
        }
        MediaWork mediaWork = this.getMediaWorkEntity(idMediaWork);
        Boolean characterIsAssociated = mediaWork.getMediaCharacters()
                .stream()
                .anyMatch( mediaCharacter -> mediaCharacter.getId().equals(idCharacter) );
        if (!characterIsAssociated) {
            throw new CustomIllegalArgumentException("The character is not currently associated with the media work");
        }
        mediaWork.removeMediaCharacter(idCharacter);
        // Because the class is annotated as transactional, changes are saved automatically.
    }

    @Override
    public void addGenreToMediaWork(Long idGenre, Long idMediaWork) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException {
        if (idGenre == null) {
            throw new CustomIllegalArgumentException("Genre id must be provided");
        }
        if (idMediaWork == null) {
            throw new CustomIllegalArgumentException("Media work id must be provided");
        }
        MediaWork mediaWork = this.getMediaWorkEntity(idMediaWork);
        Boolean mediaGenreAlreadyAssociated = mediaWork.getMediaGenres()
                .stream()
                .anyMatch( mediaGenre -> mediaGenre.getId().equals(idGenre) );
        if (mediaGenreAlreadyAssociated) {
            throw new CustomIllegalArgumentException("The genre is already associated with the media work");
        }
        MediaGenre mediaGenre = this.mediaGenreService.getMediaGenreEntity(idGenre);
        mediaWork.addMediaGenre(mediaGenre);
        // Because the class is annotated as transactional, changes are saved automatically.
    }

    @Override
    public void removeGenreFromMediaWork(Long idGenre, Long idMediaWork) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException {
        if (idGenre == null) {
            throw new CustomIllegalArgumentException("Genre id must be provided");
        }
        if (idMediaWork == null) {
            throw new CustomIllegalArgumentException("Media work id must be provided");
        }
        MediaWork mediaWork = this.getMediaWorkEntity(idMediaWork);
        Boolean mediaGenreNotAssociated = !mediaWork.getMediaGenres()
                .stream()
                .anyMatch( mediaGenre -> mediaGenre.getId().equals(idGenre) );
        if (mediaGenreNotAssociated) {
            throw new CustomIllegalArgumentException("The media work is not currently associated with the genre");
        }
        mediaWork.removeMediaGenre(idGenre);
        // Because the class is annotated as transactional, changes are saved automatically.
    }

    /**
     * Switches a MediaCharacter entity with a non-null id attribute (meaning it exists in the database) for its tracked
     * JPA version.
     *
     * Since entities to be updated are received in the form of DTOs and converted to entities manually, they
     * are not tracked by the ORM, and so it will attempt to save them as new entities instead of updating them. This
     * takes care of that by retrieving the tracked entity from the database and replacing the received instance with it.
     *
     * @param mediaWork   a MediaWork containing a list of MediaCharacters to be persisted or updated
     * @throws EntityIdentifierNotFoundException    if a MediaCharacter with a non-null id attribute doesn't have a corresponding pair in the database.
     */
    private void setJpaTrackedCharacters(MediaWork mediaWork) throws EntityIdentifierNotFoundException {
        Set<MediaCharacter> jpaTrackedCharacters = mediaWork.getMediaCharacters()
                .stream()
                .map(
                        mediaCharacter -> {
                            if (mediaCharacter.getId() == null) {
                                return mediaCharacter;
                            }
                            return this.mediaCharacterService.getMediaCharacterEntity(mediaCharacter.getId()); // throws exception if not found.
                        }
                )
                .collect(Collectors.toSet());
        mediaWork.setMediaCharacters(jpaTrackedCharacters);
    }

    /**
     * Switches a MediaGenre entity with a non-null id attribute (meaning it exists in the database) for its tracked
     * JPA version.
     *
     * Since entities to be updated are received in the form of DTOs and converted to entities manually, they
     * are not tracked by the ORM, and so it will attempt to save them as new entities instead of updating them.  This
     * takes care of that by retrieving the tracked entity from the database and replacing the received instance with it.
     *
     * @param mediaWork   a MediaWork containing a list of MediaGenres to be persisted or updated
     * @throws EntityIdentifierNotFoundException    if a genre with a non-null id attribute doesn't have a corresponding pair in the database.
     */
    private void setJpaTrackedGenres(MediaWork mediaWork) throws EntityIdentifierNotFoundException {
        Set<MediaGenre> jpaTrackedGenres = mediaWork.getMediaGenres()
                .stream()
                .map(
                        mediaGenre -> {
                            if (mediaGenre.getId() == null) {
                                return mediaGenre;
                            }
                            return this.mediaGenreService.getMediaGenreEntity(mediaGenre.getId()); // throws exception if not found.
                        }
                )
                .collect(Collectors.toSet());
        mediaWork.setMediaGenres(jpaTrackedGenres);
    }

}
