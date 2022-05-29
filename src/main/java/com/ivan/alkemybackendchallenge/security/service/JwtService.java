package com.ivan.alkemybackendchallenge.security.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Collection;
import java.util.List;

public interface JwtService {

    /**
     * Creates a JWT access token using the information from a user.
     *
     * @param username	the username of the account, unique for every user account.
     * @param roles		the list of roles or authorities the user account has in the system.
     * @param issuer	the request url.
     * @return			a JWT in the form of a String.
     */
    String createAccessToken(String username, List<String> roles, String issuer);

    /**
     * Veryfies the validity of a token and returns its decoded form.
     *
     * @param 	token	the token to analize
     * @return	the decoded token with it's information visible.
     * @throws JWTVerificationException
     */
    DecodedJWT verify(String token) throws JWTVerificationException;

    /**
     * Retrieves the username or "subject" from a JWT
     *
     * @param token a JWT
     * @return the username or "subject" from the token.
     */
    String getUsername(String token);

    /**
     * Retrieves the user's authorities or roles from a JWT
     *
     * @param token	a JWT
     * @return	the names of the user's authorities or roles
     */
    Collection<String> getAppUserRoles(String token);

}
