package com.ivan.alkemybackendchallenge.security.dto.converter;

import java.util.Collection;
import java.util.List;

/**
 * Interface for all converters that work with specific pairs of dto-entity objects.
 *
 * @param <T>   the DTO class
 * @param <R>   the Entity class
 */
public interface DtoEntityConverter <T, R> {
    R convertToEntity(T dto);
    T convertToDto(R entity);
    List<R> convertToEntityList(Collection<T> dtos);
    List<T> convertToDtoList(Collection<R> entities);
}
