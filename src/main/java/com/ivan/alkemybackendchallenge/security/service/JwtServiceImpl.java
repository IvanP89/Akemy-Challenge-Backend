package com.ivan.alkemybackendchallenge.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String createAccessToken(String username, List<String> roles, String issuer) {
        String accessToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.ACCESS_TOKEN_DURATION ))
                .withIssuer(issuer)
                .withClaim(SecurityConstants.CLAIM_USER_ROLES_NAME, roles)
                .sign(this.getSignAlgorithm());

        return accessToken;
    }

    @Override
    public DecodedJWT verify(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(this.getSignAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token); // throws exception
        return decodedJWT;
    }

    @Override
    public String getUsername(String token) {
        DecodedJWT decodedJWT = this.verify(token);
        return decodedJWT.getSubject();
    }

    @Override
    public Collection<String> getAppUserRoles(String token) {
        DecodedJWT decodedJWT = this.verify(token);
        String[] rolesArr = decodedJWT.getClaim(SecurityConstants.CLAIM_USER_ROLES_NAME).asArray(String.class);
        return Arrays.asList(rolesArr);
    }

    /**
     * Generates an Algorithm to be used to sign a token that's being created.
     *
     * @return	An algorithm for token signing.
     */
    private Algorithm getSignAlgorithm() {
        return Algorithm.HMAC256(SecurityConstants.SECRET_WORD.getBytes());
    }

}
