package com.ivan.alkemybackendchallenge.feature.resource;

import com.ivan.alkemybackendchallenge.feature.dto.data.MediaWorkDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.ReducedMediaWorkDto;
import com.ivan.alkemybackendchallenge.feature.service.MediaWorkService;
import com.ivan.alkemybackendchallenge.feature.utility.FeatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(FeatureConstants.Endpoint.MEDIA_WORK)
public class MediaWorkController {

    private final MediaWorkService mediaWorkService;

    @Autowired
    public MediaWorkController(MediaWorkService mediaWorkService) {
        this.mediaWorkService = mediaWorkService;
    }

    // RETURNS THE FULL ENTITY WITH ITS COLLECTIONS
    // NEW CHARACTERS ARE CREATED, EXISTING ONES ARE UPDATED
    @PostMapping
    public ResponseEntity<MediaWorkDto> createMediaWork(@RequestBody MediaWorkDto dto) {
        MediaWorkDto createdMediaWork = this.mediaWorkService.createMediaWork(dto);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(FeatureConstants.Endpoint.MEDIA_WORK)
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(createdMediaWork);
    }

    // RETURNS ONLY THE IMAGE, TITLE AND RELEASE DATE
    // IF NO PARAMS ARE RECEIVED, RETURNS ALL MEDIA WORKS
    @GetMapping
    public ResponseEntity<List<ReducedMediaWorkDto>> getMediaWork(@RequestParam(required = false) String name,
                                                                 @RequestParam(required = false) Long genre,
                                                                 @RequestParam(required = false) String order) {

        List<ReducedMediaWorkDto> results = this.mediaWorkService.getMediaWork(name, genre, order);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(FeatureConstants.Endpoint.MEDIA_WORK)
                        .toUriString()
        );
        return ResponseEntity.ok(results);
    }

    // RETURNS THE FULL ENTITY WITH ITS COLLECTIONS
    // CHARACTER COLLECTIONS ARE NOT TO BE UPDATED
    @PutMapping
    public ResponseEntity<MediaWorkDto> updateMediaWork(@RequestBody MediaWorkDto dto) {
        MediaWorkDto updatedMediaWork = this.mediaWorkService.updateMediaWork(dto);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(FeatureConstants.Endpoint.MEDIA_WORK)
                        .toUriString()
        );
        return ResponseEntity.ok().body(updatedMediaWork);
    }

    @DeleteMapping(path = "{idMovie}")
    public ResponseEntity<?> deleteMediaWork(@PathVariable("idMovie") Long idMovie) {
        this.mediaWorkService.deleteMediaWork(idMovie);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{idMovie}/characters/{idCharacter}")
    public ResponseEntity<MediaWorkDto> addCharacterToMediaWork(@PathVariable("idMovie") Long idMovie,
                                                                @PathVariable("idCharacter") Long idCharacter) {

        this.mediaWorkService.addCharacterToMediaWork(idCharacter, idMovie);
        MediaWorkDto updatedMediaWork = this.mediaWorkService.getMediaWork(idMovie);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(FeatureConstants.Endpoint.MEDIA_WORK)
                        .toUriString()
        );
        return ResponseEntity.ok().body(updatedMediaWork);
    }

    @DeleteMapping(path = "{idMovie}/characters/{idCharacter}")
    public ResponseEntity<?> removeCharacterFromMediaWork(@PathVariable("idMovie") Long idMovie,
                                                          @PathVariable("idCharacter") Long idCharacter) {

        this.mediaWorkService.removeCharacterFromMediaWork(idCharacter, idMovie);
        MediaWorkDto updatedMediaWork = this.mediaWorkService.getMediaWork(idMovie);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(FeatureConstants.Endpoint.MEDIA_WORK)
                        .toUriString()
        );
        return ResponseEntity.ok().body(updatedMediaWork);
    }

}
