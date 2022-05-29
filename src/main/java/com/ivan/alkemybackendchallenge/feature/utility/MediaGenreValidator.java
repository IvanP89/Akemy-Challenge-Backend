package com.ivan.alkemybackendchallenge.feature.utility;

import com.ivan.alkemybackendchallenge.feature.domain.MediaGenre;
import com.ivan.alkemybackendchallenge.feature.repository.MediaGenreRepository;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.utility.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Used to validate the attributes of a new Genre to be saved in the database.
 */
@Component
public class MediaGenreValidator implements EntityValidator<MediaGenre> {

    private final MediaGenreRepository mediaGenreRepository;

    @Autowired
    public MediaGenreValidator(MediaGenreRepository mediaGenreRepository) {
        this.mediaGenreRepository = mediaGenreRepository;
    }

    @Override
    public void validateForCreation(MediaGenre mediaGenre) throws CustomIllegalArgumentException, EntityAlreadyExistsException {
        this.validateForUpdate(mediaGenre);
        if (this.mediaGenreRepository.existsByNameIgnoreCase(mediaGenre.getName())) {
            throw new EntityAlreadyExistsException("Genre " + mediaGenre.getName() + " already exists");
        }
        if (mediaGenre.getId() != null) {
            throw new CustomIllegalArgumentException("Entities cannot be created with a custom id");
        }
        if (mediaGenre.getMediaWorks() != null && !mediaGenre.getMediaWorks().isEmpty()) {
            throw new CustomIllegalArgumentException("Genres cannot be created with attached media work collection, "
                    + "they must be added to each media work after creation");
        }
    }

    @Override
    public void validateForUpdate(MediaGenre mediaGenre) throws CustomIllegalArgumentException {
        if (mediaGenre.getName() == null) {
            throw new CustomIllegalArgumentException("Genre name not provided");
        }
        if (mediaGenre.getName().length() > FeatureConstants.GENRE_NAME_LENGTH) {
            throw new CustomIllegalArgumentException("Genre name too long (" + FeatureConstants.GENRE_NAME_LENGTH + " characters max)");
        }
        if (mediaGenre.getImageUrl().length() > FeatureConstants.IMAGE_URL_LENGTH) {
            throw new CustomIllegalArgumentException("Image url too long (" + FeatureConstants.IMAGE_URL_LENGTH + " characters max)");
        }
    }
}
