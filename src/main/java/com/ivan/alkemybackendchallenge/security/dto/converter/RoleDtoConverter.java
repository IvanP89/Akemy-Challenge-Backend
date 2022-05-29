package com.ivan.alkemybackendchallenge.security.dto.converter;

import com.ivan.alkemybackendchallenge.security.domain.Role;
import com.ivan.alkemybackendchallenge.security.dto.data.RoleDto;
import org.springframework.stereotype.Component;

@Component
public class RoleDtoConverter extends Converter<RoleDto, Role> {

    public RoleDtoConverter() {
        super(RoleDtoConverter::convertFromDto, RoleDtoConverter::convertFromEntity);
    }

    private static Role convertFromDto(RoleDto dto) {
        return new Role(dto.getId(), dto.getName());
    }

    private static RoleDto convertFromEntity(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }

}
