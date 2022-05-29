package com.ivan.alkemybackendchallenge.feature.resource;

import com.ivan.alkemybackendchallenge.feature.dto.data.MediaGenreDto;
import com.ivan.alkemybackendchallenge.feature.service.MediaGenreService;
import com.ivan.alkemybackendchallenge.feature.utility.FeatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(FeatureConstants.Endpoint.MEDIA_GENRE)
public class MediaGenreController {

    private final MediaGenreService mediaGenreService;

    @Autowired
    public MediaGenreController(MediaGenreService mediaGenreService) {
        this.mediaGenreService = mediaGenreService;
    }

    @PostMapping
    public ResponseEntity<MediaGenreDto> createGenre(@RequestBody MediaGenreDto dto) {
        MediaGenreDto createdGenre = this.mediaGenreService.createGenre(dto);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(FeatureConstants.Endpoint.MEDIA_GENRE)
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(createdGenre);
    }

}
