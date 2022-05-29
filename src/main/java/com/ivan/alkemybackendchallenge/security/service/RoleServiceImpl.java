package com.ivan.alkemybackendchallenge.security.service;

import com.ivan.alkemybackendchallenge.security.domain.Role;
import com.ivan.alkemybackendchallenge.security.dto.data.RoleDto;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;
import com.ivan.alkemybackendchallenge.security.repository.RoleRepository;
import com.ivan.alkemybackendchallenge.security.dto.converter.DtoEntityConverter;
import com.ivan.alkemybackendchallenge.security.utility.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final EntityValidator<Role> roleValidator;
    private final DtoEntityConverter<RoleDto, Role> roleDtoConverter;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, EntityValidator<Role> roleValidator,
                           DtoEntityConverter<RoleDto, Role> roleDtoConverter) {

        this.roleRepository = roleRepository;
        this.roleValidator = roleValidator;
        this.roleDtoConverter = roleDtoConverter;
    }

    @Override
    public RoleDto saveRole(RoleDto roleDto) throws CustomIllegalArgumentException, EntityAlreadyExistsException {
        Role role = this.roleDtoConverter.convertToEntity(roleDto);
        this.roleValidator.validateForCreation(role); // Throws exceptions.
        role = this.roleRepository.save(role);
        return this.roleDtoConverter.convertToDto(role);
    }

    @Override
    public Role getRole(String roleName) throws EntityIdentifierNotFoundException {
        return this.roleRepository.findByName(roleName)
                .orElseThrow( () -> new EntityIdentifierNotFoundException("Role not found") );
    }

}
