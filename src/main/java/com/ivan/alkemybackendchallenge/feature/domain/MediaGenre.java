package com.ivan.alkemybackendchallenge.feature.domain;

import com.ivan.alkemybackendchallenge.feature.utility.FeatureConstants;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "media_genre")
public class MediaGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_genre_seq")
    @SequenceGenerator(name = "media_genre_seq", sequenceName = "media_genre_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 80)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany(mappedBy = "mediaGenres")
    private Set<MediaWork> mediaWorks = new LinkedHashSet<>();

    public MediaGenre() {
        this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
    }

    public MediaGenre(Long id, String name, String imageUrl, Set<MediaWork> mediaWorks) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        this.mediaWorks = mediaWorks;
    }

    public MediaGenre(String name, String imageUrl, Set<MediaWork> mediaWorks) {
        this.name = name;
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        this.mediaWorks = mediaWorks;
    }

    public MediaGenre(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
    }

    public Long getId() {
        return id;
    }

    public MediaGenre setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MediaGenre setName(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MediaGenre setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        return this;
    }

    public Set<MediaWork> getMediaWorks() {
        return mediaWorks;
    }

    public MediaGenre setMediaWorks(Set<MediaWork> mediaWorks) {
        this.mediaWorks = mediaWorks;
        return this;
    }

    public MediaGenre addMediaWork(MediaWork mediaWork) {
        this.mediaWorks.add(mediaWork);
        return this;
    }

    public MediaGenre removeMediaWork(Long idMediaWork) {
        this.mediaWorks.removeIf( mediaWork -> mediaWork.getId().equals(idMediaWork));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaGenre)) return false;
        MediaGenre that = (MediaGenre) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}