package com.ivan.alkemybackendchallenge.feature.dto.converter;

import com.ivan.alkemybackendchallenge.feature.domain.MediaCharacter;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaCharacterDto;
import com.ivan.alkemybackendchallenge.security.dto.converter.Converter;
import org.springframework.stereotype.Component;

@Component("characterConverter")
public class MediaCharacterDtoConverter extends Converter<MediaCharacterDto, MediaCharacter> {

    public MediaCharacterDtoConverter() {
        super(MediaCharacterDtoConverter::convertFromDto, MediaCharacterDtoConverter::convertFromEntity);
    }

    /**
     * Converts everything except the mediaWork Collection, which is set as empty.
     *
     * @param dto all the entity data including its mediaWork Collection (to be converted elsewhere).
     * @return the entity without its mediaWork Collection (to be added somewhere else).
     */
    private static MediaCharacter convertFromDto(MediaCharacterDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getName() != null) {
            dto.setName( dto.getName().toLowerCase() );
        }
        return new MediaCharacter(
                dto.getId(),
                dto.getImageUrl(),
                dto.getName(),
                dto.getAge(),
                dto.getBodyWeight(),
                dto.getHistory()
        );
    }

    /**
     * Converts everything except the mediaWork Collection, which is set as empty.
     *
     * @param mediaCharacter    the complete entity
     * @return  the DTO without its mediaWork Collection (to be added somewhere else).
     */
    private static MediaCharacterDto convertFromEntity(MediaCharacter mediaCharacter) {
        if (mediaCharacter == null) {
            return null;
        }
        return new MediaCharacterDto(
                mediaCharacter.getId(),
                mediaCharacter.getImageUrl(),
                mediaCharacter.getName(),
                mediaCharacter.getAge(),
                mediaCharacter.getBodyWeight(),
                mediaCharacter.getHistory()
        );
    }

}
