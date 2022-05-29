package com.ivan.alkemybackendchallenge.security.service;

import com.ivan.alkemybackendchallenge.security.dto.data.AppUserDto;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;

import java.util.List;

public interface AppUserService {

    AppUserDto saveAppUser(AppUserDto appUserDto) throws CustomIllegalArgumentException, EntityIdentifierNotFoundException, EntityAlreadyExistsException;
    void addRoleToAppUser(String username, String roleName) throws EntityIdentifierNotFoundException;
    List<AppUserDto> getUsers();
    List<String> getRoleNames(String username) throws EntityIdentifierNotFoundException;

}
