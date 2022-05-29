package com.ivan.alkemybackendchallenge.security.service;

import com.ivan.alkemybackendchallenge.security.domain.Role;
import com.ivan.alkemybackendchallenge.security.dto.data.RoleDto;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;

public interface RoleService {

    RoleDto saveRole(RoleDto roleDto) throws CustomIllegalArgumentException, EntityAlreadyExistsException;
    Role getRole(String roleName) throws EntityIdentifierNotFoundException;

}
