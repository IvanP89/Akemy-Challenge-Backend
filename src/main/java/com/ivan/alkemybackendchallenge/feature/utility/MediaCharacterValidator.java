package com.ivan.alkemybackendchallenge.feature.utility;

import com.ivan.alkemybackendchallenge.feature.domain.MediaCharacter;
import com.ivan.alkemybackendchallenge.feature.repository.MediaCharacterRepository;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.utility.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Used to validate the attributes of a new Character to be saved in the database.
 */
@Component
public class MediaCharacterValidator implements EntityValidator<MediaCharacter> {

    private final MediaCharacterRepository mediaCharacterRepository;

    @Autowired
    public MediaCharacterValidator(MediaCharacterRepository mediaCharacterRepository) {
        this.mediaCharacterRepository = mediaCharacterRepository;
    }

    @Override
    public void validateForCreation(MediaCharacter mediaCharacter) throws CustomIllegalArgumentException, EntityAlreadyExistsException {
        this.validateForUpdate(mediaCharacter);
        if (this.mediaCharacterRepository.existsByNameIgnoreCase(mediaCharacter.getName())) {
            throw new EntityAlreadyExistsException("Character " + mediaCharacter.getName() + " already exists");
        }
        if (mediaCharacter.getId() != null) {
            throw new CustomIllegalArgumentException("Entities cannot be created with a custom id");
        }
        if (mediaCharacter.getMediaWorks() != null && !mediaCharacter.getMediaWorks().isEmpty()) {
            throw new CustomIllegalArgumentException("Characters cannot be created with attached media work collection, "
                    + "they must be added to each media work after creation");
        }
    }

    @Override
    public void validateForUpdate(MediaCharacter mediaCharacter) throws CustomIllegalArgumentException {
        if (mediaCharacter.getName() == null) {
            throw new CustomIllegalArgumentException("Character name not provided");
        }
        if (mediaCharacter.getName().length() > FeatureConstants.CHARACTER_NAME_LENGTH) {
            throw new CustomIllegalArgumentException("Character name too long (" + FeatureConstants.CHARACTER_NAME_LENGTH + " characters max)");
        }
        if (mediaCharacter.getHistory() != null && mediaCharacter.getHistory().length() > FeatureConstants.CHARACTER_HISTORY_LENGTH) {
            throw new CustomIllegalArgumentException("Character history too long (" + FeatureConstants.CHARACTER_HISTORY_LENGTH + " characters max)");
        }
        if (mediaCharacter.getImageUrl().length() > FeatureConstants.IMAGE_URL_LENGTH) {
            throw new CustomIllegalArgumentException("Image url too long (" + FeatureConstants.IMAGE_URL_LENGTH + " characters max)");
        }
    }
}
