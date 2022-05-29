package com.ivan.alkemybackendchallenge.feature.repository;

import com.ivan.alkemybackendchallenge.feature.domain.MediaCharacter;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaCharacterRepository extends JpaRepository <MediaCharacter, Long> {

    @EntityGraph(attributePaths = { "mediaWorks" })
    Optional<MediaCharacter> findById(Long id);

//    @EntityGraph(attributePaths = { "mediaWorks" })
//    List<MediaCharacter> findByNameIgnoreCaseAndAgeAndMediaWorks_Id(String name, Integer age, Long id);

//    @EntityGraph(attributePaths = { "mediaWorks" })
    @Query("select distinct c from MediaCharacter c join fetch c.mediaWorks w "
            + "where (:name is null or c.name like %:name%) "
            + "and (:age is null or c.age=:age) "
            + "and (:idmedia is null or w.id=:idmedia)")
    List<MediaCharacter> findByNameIgnoreCaseAndAgeAndMediaWorks_Id(
            @Param("name") String name,
            @Param("age") Integer age,
            @Param("idmedia") Long id
    );

    List<MediaCharacter> findAll(Example e);

    @EntityGraph(attributePaths = { "mediaWorks" })
    List<MediaCharacter> findAll();

    void deleteById(Long id);

    Boolean existsByNameIgnoreCase(String name);

}
