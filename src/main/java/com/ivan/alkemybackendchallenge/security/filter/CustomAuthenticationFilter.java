package com.ivan.alkemybackendchallenge.security.filter;

import com.ivan.alkemybackendchallenge.security.service.JwtService;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import com.ivan.alkemybackendchallenge.security.utility.CustomHttpServletResponseUtility;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter(SecurityConstants.USERNAME_PARAM_NAME);
        if (username == null) {
            throw new BadCredentialsException("The request does not contain the username parameter");
        }
        String password = request.getParameter(SecurityConstants.PASSWORD_PARAM_NAME);
        if (password == null) {
            throw new BadCredentialsException("The request does not contain the password parameter");
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String username = this.getAuthenticatedUserUsername(authResult);
        List<String> roles = this.getAuthenticatedUserRoles(authResult);
        String issuer = request.getRequestURL().toString();

        String accessToken = this.jwtService.createAccessToken(username, roles, issuer);

        CustomHttpServletResponseUtility.addTokenToResponseBody(accessToken, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        CustomHttpServletResponseUtility.addErrorToResponseBody(request, response, failed.getMessage(), HttpStatus.FORBIDDEN);
//        super.unsuccessfulAuthentication(request, response, failed);
    }

    private UserDetails getAuthenticatedUser(Authentication authentication) {
        return (UserDetails) authentication.getPrincipal();
    }

    private String getAuthenticatedUserUsername(Authentication authentication) {
        return this.getAuthenticatedUser(authentication).getUsername();
    }

    private List<String> getAuthenticatedUserRoles(Authentication authentication) {
        return this.getAuthenticatedUser(authentication)
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

}
