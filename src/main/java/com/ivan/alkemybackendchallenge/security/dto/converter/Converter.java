package com.ivan.alkemybackendchallenge.security.dto.converter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Generic parent class for all converters that work with specific pairs of dto-entity objects.
 *
 * Inherited classes must implement two static methods to perform conversions between single instances of DTOs and
 * entities and provide those methods to this class' constructor. This class will then use those methods to also
 * implement the ones to convert between collections of the specified objects.
 *
 * @param <T>   the DTO class
 * @param <R>   the Entity class
 */
public class Converter <T, R> implements DtoEntityConverter<T, R> {

    private final Function<T, R> fromDtoToEntity;
    private final Function<R, T> fromEntityToDto;

    public Converter(Function<T, R> fromDtoToEntity, Function<R, T> fromEntityToDto) {
        this.fromDtoToEntity = fromDtoToEntity;
        this.fromEntityToDto = fromEntityToDto;
    }

    @Override
    public final R convertToEntity(T dto) {
        return this.fromDtoToEntity.apply(dto);
    }

    @Override
    public final T convertToDto(R entity) {
        return this.fromEntityToDto.apply(entity);
    }

    @Override
    public final List<R> convertToEntityList(Collection<T> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }
        return dtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Set<R> convertToEntitySet(Collection<T> dtos) {
        if (dtos == null) {
            return new LinkedHashSet<>();
        }
        return dtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public final List<T> convertToDtoList(Collection<R> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Set<T> convertToDtoSet(Collection<R> entities) {
        if (entities == null) {
            return new LinkedHashSet<>();
        }
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

}
