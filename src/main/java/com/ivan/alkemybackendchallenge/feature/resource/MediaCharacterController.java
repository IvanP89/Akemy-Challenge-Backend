package com.ivan.alkemybackendchallenge.feature.resource;

import com.ivan.alkemybackendchallenge.feature.dto.data.MediaCharacterDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.ReducedMediaCharacterDto;
import com.ivan.alkemybackendchallenge.feature.service.MediaCharacterService;
import com.ivan.alkemybackendchallenge.feature.utility.FeatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
public class MediaCharacterController {

    private final MediaCharacterService mediaCharacterService;

    @Autowired
    public MediaCharacterController(MediaCharacterService mediaCharacterService) {
        this.mediaCharacterService = mediaCharacterService;
    }

    // RETURNS THE FULL ENTITY WITH ITS COLLECTIONS
    @PostMapping
    public ResponseEntity<MediaCharacterDto> createMediaCharacter(@RequestBody MediaCharacterDto dto) {
        MediaCharacterDto createdCharacter = this.mediaCharacterService.createMediaCharacter(dto);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(createdCharacter);
    }

    // RETURNS ONLY THE NAME AND THE IMAGE
    // IF NO PARAMS ARE RECEIVED, RETURNS ALL CHARACTERS.
    @GetMapping
    public ResponseEntity<List<ReducedMediaCharacterDto>> getMediaCharacter(@RequestParam(required = false) String name,
                                                                            @RequestParam(required = false) Integer age,
                                                                            @RequestParam(required = false) Long movies) {

        List<ReducedMediaCharacterDto> mediaCharacterDtos = this.mediaCharacterService.getMediaCharacters(name, age, movies);
        return ResponseEntity.ok().body(mediaCharacterDtos);
    }

    // RETURNS THE FULL ENTITY WITH ITS COLLECTIONS
    // MEDIA COLLECTIONS ARE NOT TO BE UPDATED
    @PutMapping
    public ResponseEntity<MediaCharacterDto> updateMediaCharacter(@RequestBody MediaCharacterDto dto) {
        MediaCharacterDto updatedCharacter = this.mediaCharacterService.updateMediaCharacter(dto);
        return ResponseEntity.ok().body(updatedCharacter);
    }

    @DeleteMapping("{idCharacter}")
    public ResponseEntity<?> deleteMediaCharacter(@PathVariable("idCharacter") Long idCharacter) {
        this.mediaCharacterService.deleteMediaCharacter(idCharacter);
        return ResponseEntity.ok().build();
    }

}
