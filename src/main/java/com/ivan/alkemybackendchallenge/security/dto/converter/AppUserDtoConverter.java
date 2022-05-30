package com.ivan.alkemybackendchallenge.security.dto.converter;

import com.ivan.alkemybackendchallenge.security.domain.AppUser;
import com.ivan.alkemybackendchallenge.security.domain.Role;
import com.ivan.alkemybackendchallenge.security.dto.data.AppUserDto;
import com.ivan.alkemybackendchallenge.security.dto.data.RoleDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AppUserDtoConverter extends Converter<AppUserDto, AppUser> {

    public AppUserDtoConverter() {
        super(AppUserDtoConverter::convertFromDto, AppUserDtoConverter::convertFromEntity);
    }

    private static AppUser convertFromDto(AppUserDto dto) {
        if (dto == null) {
            return null;
        }
        Collection<Role> roles = new RoleDtoConverter().convertToEntityList( dto.getRoles() );
        return new AppUser(
                dto.getId(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAlias(),
                roles
        );
    }

    private static AppUserDto convertFromEntity(AppUser appUser) {
        if (appUser == null) {
            return null;
        }
        Collection<RoleDto> roleDtos = new RoleDtoConverter().convertToDtoList( appUser.getRoles() );
        return new AppUserDto(
                appUser.getId(),
                appUser.getEmail(),
                appUser.getPassword(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getAlias(),
                roleDtos
        );
    }

}
