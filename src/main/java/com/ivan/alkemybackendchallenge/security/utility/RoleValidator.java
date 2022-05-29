package com.ivan.alkemybackendchallenge.security.utility;

import com.ivan.alkemybackendchallenge.security.domain.Role;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RoleValidator implements EntityValidator<Role> {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void validateForCreation(Role role) throws CustomIllegalArgumentException, EntityAlreadyExistsException {
        this.validateForUpdate(role);
        if ( this.roleRepository.existsByName(role.getName()) ) {
            throw new EntityAlreadyExistsException("Role name already exists in the database.");
        }
    }

    @Override
    public void validateForUpdate(Role role) throws CustomIllegalArgumentException {
        if(role == null) {
            throw new CustomIllegalArgumentException("User data not provided.");
        }
        if (role.getName() == null) {
            throw new CustomIllegalArgumentException("Role name not provided.");
        }
    }
}
