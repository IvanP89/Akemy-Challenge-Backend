package com.ivan.alkemybackendchallenge.feature.repository;

import com.ivan.alkemybackendchallenge.feature.domain.MediaGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaGenreRepository extends JpaRepository<MediaGenre, Long> {

    Boolean existsByNameIgnoreCase(String name);

//    @EntityGraph(attributePaths = { "mediaWorks" })
    Optional<MediaGenre> findById(Long id);

    Optional<MediaGenre> findByNameIgnoreCase(String name);

}
