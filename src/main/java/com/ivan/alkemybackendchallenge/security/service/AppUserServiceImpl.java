package com.ivan.alkemybackendchallenge.security.service;

import com.ivan.alkemybackendchallenge.security.domain.AppUser;
import com.ivan.alkemybackendchallenge.security.domain.Role;
import com.ivan.alkemybackendchallenge.security.dto.converter.DtoEntityConverter;
import com.ivan.alkemybackendchallenge.security.dto.data.AppUserDto;
import com.ivan.alkemybackendchallenge.security.exception.CustomIllegalArgumentException;
import com.ivan.alkemybackendchallenge.security.exception.EmailException;
import com.ivan.alkemybackendchallenge.security.exception.EntityAlreadyExistsException;
import com.ivan.alkemybackendchallenge.security.exception.EntityIdentifierNotFoundException;
import com.ivan.alkemybackendchallenge.security.repository.AppUserRepository;

import com.ivan.alkemybackendchallenge.security.utility.*;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final DtoEntityConverter<AppUserDto, AppUser> appUserDtoConverter;
    private final EntityValidator<AppUser> appUserValidator;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              RoleService roleService,
                              PasswordEncoder passwordEncoder,
                              EmailService emailService,
                              DtoEntityConverter<AppUserDto, AppUser> appUserDtoConverter,
                              EntityValidator<AppUser> appUserValidator) {

        this.appUserRepository = appUserRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.appUserDtoConverter = appUserDtoConverter;
        this.appUserValidator = appUserValidator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = this.appUserRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Account doesn't exist")
                );
        return new UserDetailsDecorator<>(appUser);
    }

    @Override
    public AppUserDto saveAppUser(AppUserDto appUserDto) throws CustomIllegalArgumentException,
            EntityIdentifierNotFoundException, EntityAlreadyExistsException {

        AppUser appUser = this.appUserDtoConverter.convertToEntity(appUserDto);
        this.appUserValidator.validateForCreation(appUser); // Throws exception
        appUser.setPassword( this.passwordEncoder.encode(appUser.getPassword()) );
        Role defaultRole = this.roleService.getRole(SecurityConstants.DEFAULT_USER_ROLE); // Throws exception
        appUser.addRole(defaultRole);
        appUser = appUserRepository.save(appUser);
        // UNCOMMENT TO ENABLE THE WELCOME EMAIL FUNCTIONALITY AFTER SETTING THE SENDGRID VALUES IN THE
        // application.properties FILE.
//        try {
//            this.emailService.sendWelcomeEmail( appUser.getEmail() );
//        } catch (EmailException e) {
//            // TODO: for future versions, do something when the email client fails.
//        }
        return this.appUserDtoConverter.convertToDto(appUser);
    }

    @Override
    public void addRoleToAppUser(String username, String roleName) throws EntityIdentifierNotFoundException {
        AppUser appUser = this.appUserRepository.findByEmail(username)
                .orElseThrow( () -> new EntityIdentifierNotFoundException("User not found") );
        Role role = this.roleService.getRole(roleName); // Throws exception
        appUser.addRole(role);
    }

    @Override
    public List<AppUserDto> getUsers() {
        return this.appUserDtoConverter.convertToDtoList( this.appUserRepository.findAll() );
    }

    @Override
    public List<String> getRoleNames(String username) throws EntityIdentifierNotFoundException {
        AppUser appUser = this.appUserRepository.findByEmail(username)
                .orElseThrow( () -> new EntityIdentifierNotFoundException("User not found") );
        return appUser.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

}
