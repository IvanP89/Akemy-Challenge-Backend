package com.ivan.alkemybackendchallenge.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ivan.alkemybackendchallenge.security.service.JwtService;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import com.ivan.alkemybackendchallenge.security.utility.CustomHttpServletResponseUtility;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public CustomAuthorizationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Allows access to secured paths without login if the request comes with an access token.
     *
     * Basically if a request lands on a path that's not open for all, it checks whether the request comes with
     * an authentication token attached, and if it is, it retrieves it and uses it to authenticate the user within
     * the app, then the flow of the app continues as if the user had performed a login action.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Boolean requestDoesNotComeFromFreeAccessEndpoint =
                !request.getServletPath().equals(SecurityConstants.Endpoint.LOGIN)
                && !request.getServletPath().equals(SecurityConstants.Endpoint.REGISTER);

        if (requestDoesNotComeFromFreeAccessEndpoint && authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                UsernamePasswordAuthenticationToken authenticationToken = this.generateContextAuthToken(authorizationHeader); // Throws exception if the token verification fails.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                CustomHttpServletResponseUtility.addErrorToResponseBody(request, response, e.getMessage(), HttpStatus.FORBIDDEN);
            }
        } else if (requestDoesNotComeFromFreeAccessEndpoint
                && (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))) {

            CustomHttpServletResponseUtility.addErrorToResponseBody(request, response, SecurityConstants.TOKEN_NOT_FOUND_MSG, HttpStatus.FORBIDDEN);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Generates a new Authentication token to be used to change the currently authenticated principal.
     *
     * It uses the information contained in the access token received within the request's authorization header.
     *
     * @param authorizationHeader   the authorization header of the request.
     * @return  a new Authentication token representing the new authenticated principal.
     * @throws JWTVerificationException if the verification of the access token within the header fails.
     */
    private UsernamePasswordAuthenticationToken generateContextAuthToken(String authorizationHeader)
            throws JWTVerificationException {

        String accessToken = authorizationHeader.substring("Bearer ".length());
        this.jwtService.verify(accessToken); // Throws exception if verification fails.
        String username = this.jwtService.getUsername(accessToken);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        this.jwtService.getAppUserRoles(accessToken).forEach(
                role -> {
                    authorities.add( new SimpleGrantedAuthority(role) );
                }
        );
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

} // Class end
