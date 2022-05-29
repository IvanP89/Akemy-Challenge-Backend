package com.ivan.alkemybackendchallenge.feature.domain;

import com.ivan.alkemybackendchallenge.feature.utility.FeatureConstants;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "media_character")
public class MediaCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_character_seq")
    @SequenceGenerator(name = "media_character_seq", sequenceName = "media_character_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "name", nullable = false, unique = true, length = 80)
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "body_weight")
    private Double bodyWeight;

    @Column(name = "history", length = 80000)
    private String history;

    @ManyToMany(mappedBy = "mediaCharacters")
    private Set<MediaWork> mediaWorks = new LinkedHashSet<>();

    public MediaCharacter() {
        this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
    }

    public MediaCharacter(Long id, String imageUrl, String name, Integer age, Double bodyWeight, String history,
                          Set<MediaWork> mediaWorks) {

        this.id = id;
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        this.name = name;
        this.age = age;
        this.bodyWeight = bodyWeight;
        this.history = history;
        this.mediaWorks = mediaWorks;
    }

    public MediaCharacter(String imageUrl, String name, Integer age, Double bodyWeight, String history,
                          Set<MediaWork> mediaWorks) {

        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        this.name = name;
        this.age = age;
        this.bodyWeight = bodyWeight;
        this.history = history;
        this.mediaWorks = mediaWorks;
    }

    public MediaCharacter(Long id, String imageUrl, String name, Integer age, Double bodyWeight, String history) {
        this.id = id;
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        this.name = name;
        this.age = age;
        this.bodyWeight = bodyWeight;
        this.history = history;
    }

    public Long getId() {
        return id;
    }

    public MediaCharacter setId(Long id) {
        this.id = id;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MediaCharacter setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        if (this.imageUrl == null) {
            this.imageUrl = FeatureConstants.DEFAULT_IMAGE_URL;
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public MediaCharacter setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public MediaCharacter setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Double getBodyWeight() {
        return bodyWeight;
    }

    public MediaCharacter setBodyWeight(Double bodyWeight) {
        this.bodyWeight = bodyWeight;
        return this;
    }

    public String getHistory() {
        return history;
    }

    public MediaCharacter setHistory(String history) {
        this.history = history;
        return this;
    }

    public Set<MediaWork> getMediaWorks() {
        return mediaWorks;
    }

    public MediaCharacter setMediaWorks(Set<MediaWork> mediaWorks) {
        this.mediaWorks = mediaWorks;
        return this;
    }

    public MediaCharacter addMediaWork(MediaWork mediaWork) {
        if (mediaWorks != null) {
            this.mediaWorks.add(mediaWork);
        }
        return this;
    }

    public MediaCharacter removeMediaWork(Long idMediaWork) {
        this.mediaWorks.removeIf(mediaWork -> mediaWork.getId().equals(idMediaWork));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaCharacter)) return false;
        MediaCharacter that = (MediaCharacter) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}