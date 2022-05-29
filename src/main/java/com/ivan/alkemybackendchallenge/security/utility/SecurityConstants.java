package com.ivan.alkemybackendchallenge.security.utility;

public class SecurityConstants {

    // ROLES
    public static final String DEFAULT_USER_ROLE = "ROLE_USER";
    public static final String DEFAULT_ADMIN_ROLE = "ROLE_ADMIN";

    // JWT
    public static final Long ACCESS_TOKEN_DURATION = 7L * 24 * 60 * 60 * 1000; // 1 week in milliseconds.
    public static final String SECRET_WORD = "someSecretWordToSignTheTokensStoredSomewhereSafe";
    public static final String CLAIM_USER_ROLES_NAME = "roles";

    // PARAM NAMES
    public static final String USERNAME_PARAM_NAME = "username";
    public static final String PASSWORD_PARAM_NAME = "password";
    public static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

    // MESSAGES
    public static final String TOKEN_NOT_FOUND_MSG = "Authentication token required";

    // TESTING
    public static final String TEST_ADMIN_USERNAME = "admin@alkemygmail.com";
    public static final String TEST_ADMIN_PASSWORD = "1234";
    public static final String TEST_USER_USERNAME = "user@alkemygmail.com";
    public static final String TEST_USER_PASSWORD = "5678";

    public static class Endpoint {

        public static final String LOGIN = "/auth/login";
        public static final String REGISTER = "/auth/register";
        public static final String USERS = "/auth/users";

    }

}
