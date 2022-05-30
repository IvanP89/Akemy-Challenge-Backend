package com.ivan.alkemybackendchallenge.security.dto.converter;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Interface for all converters that work with specific pairs of dto-entity objects.
 *
 * @param <T>   the DTO class
 * @param <R>   the Entity class
 */
public interface DtoEntityConverter <T, R> {

    /**
     * Generates a domain instance from the data contained in the corresponding DTO.
     *
     * @param dto   the corresponding DTO for the domain entity.
     * @return  a domain-class instance.
     */
    R convertToEntity(T dto);

    /**
     * Builds a DTO from an entity.
     *
     * @param entity    a domain-class instance.
     * @return  a DTO with the entity's data.
     */
    T convertToDto(R entity);

    /**
     * Performs a batch conversion from DTOs to domain-class entities.
     *
     * @param dtos a collection of DTOs.
     * @return  a list of domain-class instances.
     */
    List<R> convertToEntityList(Collection<T> dtos);

    /**
     * Performs a batch conversion from DTOs to domain-class entities.
     *
     * @param dtos a collection of DTOs.
     * @return  a set of domain-class instances.
     */
    Set<R> convertToEntitySet(Collection<T> dtos);

    /**
     * Performs a batch conversion from domain-class instances to DTOs.
     *
     * @param entities  a collection of domain-class instances.
     * @return  a list of DTOs.
     */
    List<T> convertToDtoList(Collection<R> entities);

    /**
     * Performs a batch conversion from domain-class instances to DTOs.
     *
     * @param entities  a collection of domain-class instances.
     * @return  a set of DTOs.
     */
    Set<T> convertToDtoSet(Collection<R> entities);
}
