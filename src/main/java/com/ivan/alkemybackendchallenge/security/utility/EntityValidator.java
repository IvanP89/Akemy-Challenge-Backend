package com.ivan.alkemybackendchallenge.security.utility;

import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;

public interface EntityValidator<T> {

    /**
     * Verifies the attributes of a new object to be stored in the database.
     *
     * If there is a problem with an attribute, it throws the appropriate exception. If every attribute is acceptable,
     * nothing happens.
     *
     * @param entity    the new entity to be stored.
     * @throws IllegalArgumentException if there is an invalid attribute.
     * @throws EntityAlreadyExistsException if an object with the same identifiers already exists in the database.
     */
    void validateForCreation(T entity) throws CustomIllegalArgumentException, EntityAlreadyExistsException;

    /**
     * Verifies the attributes of an object to be updated.
     *
     * If there is a problem with an attribute, it throws the appropriate exception. If every attribute is acceptable,
     * nothing happens.
     *
     * @param entity    entity to be updated
     * @throws IllegalArgumentException if there is an invalid attribute.
     */
    void validateForUpdate(T entity) throws CustomIllegalArgumentException;

}
