package com.ivan.alkemybackendchallenge.security.utility;

import com.ivan.alkemybackendchallenge.security.domain.AppUser;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

/**
 * Used to validate the attributes of a new AppUser to be saved in the database.
 */
@Component
public class AppUserValidator implements EntityValidator<AppUser> {

    private final AppUserRepository appUserRepository;
    private final Pattern emailPattern;

    @Autowired
    public AppUserValidator(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
        this.emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
//        this.emailPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    @Override
    @Transactional(readOnly = true)
    public void validateForCreation(AppUser appUser) throws CustomIllegalArgumentException, EntityAlreadyExistsException {
        this.validateForUpdate(appUser);
        if (this.appUserRepository.existsByEmail(appUser.getEmail())) {
            throw new EntityAlreadyExistsException("Email already taken");
        }
    }

    @Override
    public void validateForUpdate(AppUser appUser) throws CustomIllegalArgumentException {
        if (appUser == null) {
            throw new CustomIllegalArgumentException("User data not provided");
        }
        if (appUser.getEmail() == null) {
            throw new CustomIllegalArgumentException("Email address not provided");
        }
        if ( !this.emailPattern.matcher(appUser.getEmail()).matches() ) {
            throw new CustomIllegalArgumentException("Invalid email format");
        }
        if (appUser.getPassword() == null) {
            throw new CustomIllegalArgumentException("Password not provided");
        }
    }

}
