package com.ivan.alkemybackendchallenge.feature.domain;

import com.ivan.alkemybackendchallenge.feature.utility.FeatureConstants;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "media_work")
public class MediaWork {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_work_seq")
    @SequenceGenerator(name = "media_work_seq", sequenceName = "media_work_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_work_type", nullable = false)
    private MediaWorkType mediaWorkType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "title", nullable = false, length = 80)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "score")
    private Double score;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "media_work_media_character",
            joinColumns = @JoinColumn(name = "media_work_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "media_character_id", referencedColumnName = "id"))
    private Set<MediaCharacter> mediaCharacters = new LinkedHashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "media_work_media_genre",
            joinColumns = @JoinColumn(name = "media_work_id"),
            inverseJoinColumns = @JoinColumn(name = "media_genre_id"))
    private Set<MediaGenre> mediaGenres = new LinkedHashSet<>();

    public MediaWork() {
        this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
    }

    public MediaWork(Long id, MediaWorkType mediaWorkType, String imageUrl, String title, LocalDate releaseDate,
                     Double score, Set<MediaCharacter> mediaCharacters) {

        this.id = id;
        this.mediaWorkType = mediaWorkType;
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        this.title = title;
        this.releaseDate = releaseDate;
        this.score = score;
        this.mediaCharacters = mediaCharacters;
    }

    public MediaWork(MediaWorkType mediaWorkType, String imageUrl, String title, LocalDate releaseDate, Double score,
                     Set<MediaCharacter> mediaCharacters) {

        this.mediaWorkType = mediaWorkType;
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        this.title = title;
        this.releaseDate = releaseDate;
        this.score = score;
        this.mediaCharacters = mediaCharacters;
    }

    public MediaWork(Long id, MediaWorkType mediaWorkType, String imageUrl, String title, LocalDate releaseDate,
                     Double score) {

        this.id = id;
        this.mediaWorkType = mediaWorkType;
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        this.title = title;
        this.releaseDate = releaseDate;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public MediaWork setId(Long id) {
        this.id = id;
        return this;
    }

    public MediaWorkType getMediaWorkType() {
        return mediaWorkType;
    }

    public MediaWork setMediaWorkType(MediaWorkType mediaWorkType) {
        this.mediaWorkType = mediaWorkType;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MediaWork setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MediaWork setTitle(String title) {
        this.title = title;
        return this;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public MediaWork setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Double getScore() {
        return score;
    }

    public MediaWork setScore(Double score) {
        this.score = score;
        return this;
    }

    public Set<MediaCharacter> getMediaCharacters() {
        return mediaCharacters;
    }

    public MediaWork setMediaCharacters(Set<MediaCharacter> mediaCharacters) {
        this.mediaCharacters = mediaCharacters;
        return this;
    }

    public MediaWork addMediaCharacter(MediaCharacter mediaCharacter) {
        this.mediaCharacters.add(mediaCharacter);
        return this;
    }

    public MediaWork removeMediaCharacter(Long idCharacter) {
        this.mediaCharacters.removeIf(mediaCharacter -> mediaCharacter.getId().equals(idCharacter));
        return this;
    }

    public Set<MediaGenre> getMediaGenres() {
        return mediaGenres;
    }

    public MediaWork setMediaGenres(Set<MediaGenre> mediaGenres) {
        this.mediaGenres = mediaGenres;
        return this;
    }

    public MediaWork addMediaGenre(MediaGenre mediaGenre) {
        this.mediaGenres.add(mediaGenre);
        return this;
    }

    public MediaWork removeMediaGenre(Long idGenre) {
        this.mediaGenres.removeIf(mediaGenre -> mediaGenre.getId().equals(idGenre));
        return this;
    }

}