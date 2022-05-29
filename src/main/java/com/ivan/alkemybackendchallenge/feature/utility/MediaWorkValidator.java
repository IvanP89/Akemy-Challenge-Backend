package com.ivan.alkemybackendchallenge.feature.utility;

import com.ivan.alkemybackendchallenge.feature.domain.MediaWork;
import com.ivan.alkemybackendchallenge.feature.repository.MediaWorkRepository;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.utility.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MediaWorkValidator implements EntityValidator<MediaWork> {
    
    private final MediaWorkRepository mediaWorkRepository;

    @Autowired
    public MediaWorkValidator(MediaWorkRepository mediaWorkRepository) {
        this.mediaWorkRepository = mediaWorkRepository;
    }

    @Override
    public void validateForCreation(MediaWork mediaWork) throws CustomIllegalArgumentException, EntityAlreadyExistsException {
        this.validateForUpdate(mediaWork);
        if (this.mediaWorkRepository.existsByTitleIgnoreCase(mediaWork.getTitle())) {
            throw new EntityAlreadyExistsException("Media work " + mediaWork.getTitle() + " already exists");
        }
        if (mediaWork.getId() != null) {
            throw new CustomIllegalArgumentException("Entities cannot be created with a custom id");
        }
    }

    @Override
    public void validateForUpdate(MediaWork mediaWork) throws CustomIllegalArgumentException {
        if (mediaWork.getTitle() == null) {
            throw new CustomIllegalArgumentException("Media work title not provided");
        }
        if (mediaWork.getTitle().length() > FeatureConstants.MEDIA_WORK_TITLE_LENGTH) {
            throw new CustomIllegalArgumentException("Media work title too long (" + FeatureConstants.MEDIA_WORK_TITLE_LENGTH + " characters max)");
        }
        if (mediaWork.getMediaWorkType() == null) {
            throw new CustomIllegalArgumentException("Illegal media work type (must be movie or tv_series)");
        }
        if (mediaWork.getImageUrl().length() > FeatureConstants.IMAGE_URL_LENGTH) {
            throw new CustomIllegalArgumentException("Image url too long (" + FeatureConstants.IMAGE_URL_LENGTH + " characters max)");
        }
        if (mediaWork.getScore() != null
                && (mediaWork.getScore() < FeatureConstants.MEDIA_WORK_SCORE_MIN_VALUE || mediaWork.getScore() > FeatureConstants.MEDIA_WORK_SCORE_MAX_VALUE)) {

            throw new CustomIllegalArgumentException(
                    "Score must be a number between "+ FeatureConstants.MEDIA_WORK_SCORE_MIN_VALUE + " and " + FeatureConstants.MEDIA_WORK_SCORE_MAX_VALUE
            );
        }
        if (mediaWork.getReleaseDate() != null && mediaWork.getReleaseDate().equals(LocalDate.MIN)) {
            throw new CustomIllegalArgumentException("Invalid release date format. The date format must be yyyy-mm-dd");
        }
    }
}
