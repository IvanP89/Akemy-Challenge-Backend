package com.ivan.alkemybackendchallenge.security.resource;

import com.ivan.alkemybackendchallenge.security.dto.data.AppUserDto;
import com.ivan.alkemybackendchallenge.security.service.AppUserService;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
public class AuthController {

    private final AppUserService appUserService;

    @Autowired
    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping(SecurityConstants.Endpoint.USERS)
    public ResponseEntity<List<AppUserDto>> getUsers() {
        return ResponseEntity.ok().body( this.appUserService.getUsers() );
    }

    @PostMapping(SecurityConstants.Endpoint.REGISTER)
    public ResponseEntity<AppUserDto> register(@RequestBody AppUserDto appUserDto) {
        appUserDto = this.appUserService.saveAppUser(appUserDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(SecurityConstants.Endpoint.REGISTER)
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(appUserDto);
    }

}
