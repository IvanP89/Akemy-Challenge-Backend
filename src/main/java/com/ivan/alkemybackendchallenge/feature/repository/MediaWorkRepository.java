package com.ivan.alkemybackendchallenge.feature.repository;

import com.ivan.alkemybackendchallenge.feature.domain.MediaWork;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaWorkRepository extends JpaRepository<MediaWork, Long> {

    @EntityGraph(attributePaths = { "mediaCharacters", "mediaGenres" })
    Optional<MediaWork> findById(Long id);

//    @EntityGraph(attributePaths = { "mediaCharacters", "mediaGenres" })
//    List<MediaWork> findByTitleIgnoreCaseAndMediaGenres_Id(String title, Long id, Sort sort);

    @Query("select distinct m from MediaWork m join fetch m.mediaGenres g "
            + "where (:title is null or m.title like %:title%) "
            + "and (:idgenre is null or g.id=:idgenre)")
    List<MediaWork> findByTitleIgnoreCaseAndMediaGenres_Id(
            @Param("title") String title,
            @Param("idgenre") Long id,
            Sort sort
    );

    @EntityGraph(attributePaths = { "mediaCharacters", "mediaGenres" })
    List<MediaWork> findAll();

    void deleteById(Long id);

    Boolean existsByTitleIgnoreCase(String title);

}
