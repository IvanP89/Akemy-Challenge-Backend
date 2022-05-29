package com.ivan.alkemybackendchallenge.security.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.alkemybackendchallenge.security.dto.data.AppUserDto;
import com.ivan.alkemybackendchallenge.security.dto.data.ErrorResponseBodyDto;
import com.ivan.alkemybackendchallenge.security.service.JwtService;
import com.ivan.alkemybackendchallenge.security.service.JwtServiceImpl;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerIntegrationTest {

    private final RestTemplate restTemplate;
    private final String registerAbsoluteURL = "http://localhost:8080/auth/register";
    private final String loginAbsoluteURL = "http://localhost:8080/auth/login";
    private Map<String, String> validUserToCreateForm;
    private MultiValueMap<String, String> validUserToLoginForm;

    @Autowired
    public AuthControllerIntegrationTest(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Generates a map with valid attributes to create a new User.
     *
     * The keys have the same names as the attributes from the class AppUserDto.
     */
    @BeforeEach
    private void initUserToCreateForm() {
        this.validUserToCreateForm = new HashMap<>();
        this.validUserToCreateForm.put("email", "johndoe@alkemygmail.com");
        this.validUserToCreateForm.put("password", "123456789");
        this.validUserToCreateForm.put("firstName", "John");
        this.validUserToCreateForm.put("lastName", "Doe");
        this.validUserToCreateForm.put("alias", "@JDoe");
    }

    /**
     * Generates a user map with valid attributes to login.
     */
    @BeforeEach
    private void initUserToLoginForm() {
        this.validUserToLoginForm = new LinkedMultiValueMap<>();
        this.validUserToLoginForm.add("username", SecurityConstants.TEST_USER_USERNAME);
        this.validUserToLoginForm.add("password", SecurityConstants.TEST_USER_PASSWORD);
    }

    @Nested
    class RegisterTest {

        @Test
        @DisplayName("Valid case")
        void registerTest() {
            ResponseEntity<AppUserDto> result = restTemplate.postForEntity(
                    registerAbsoluteURL,
                    RequestEntity.post(SecurityConstants.Endpoint.REGISTER)
                            .body(validUserToCreateForm),
                    AppUserDto.class
            );
            assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value(), "Response status is CREATED");
            assertNotNull(result.getBody(), "Response body is present");
            assertEquals(validUserToCreateForm.get("email").toLowerCase(), result.getBody().getEmail(), "Entity with the same email was stored and returned");
            assertNull(validUserToCreateForm.get("id"), "id attribute wasn't sent in the request.");
            assertNotNull(result.getBody().getId(), "Id attribute is present in the response (generated by the server)");
        }

        @Test
        @DisplayName("Mandatory attribute missing")
        void registerIllegalArgument1Test() {
            validUserToCreateForm.remove("email");
            assertNull(validUserToCreateForm.get("email"), "Email attribute removed from the user to create.");

            ResponseEntity<AppUserDto>  validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        registerAbsoluteURL,
                        RequestEntity.post(SecurityConstants.Endpoint.REGISTER)
                                .body(validUserToCreateForm),
                        AppUserDto.class
                );

            } catch (HttpClientErrorException e) {

                String responseBodyAsString = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    resultError = objectMapper.readValue(responseBodyAsString, ErrorResponseBodyDto.class);
                } catch (JsonProcessingException e2) {
                    // resultError is left as null to be handled with the first assert.
                }

            }

            assertNull(validResult, "The method call was interrupted, so the normal result is null.");
            assertNotNull(resultError, "Exception launched, caught by CustomResponseEntityExceptionHandler, error response obtained and mapped");
            assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(resultError.getStatus()), "Response status is BAD_REQUEST");
            assertEquals("Email address not provided", resultError.getMessage(), "Error message is correct");
        }

        @ParameterizedTest
        @MethodSource("com.ivan.alkemybackendchallenge.security.resource.AuthControllerIntegrationTest#getInvalidEmails")
        @DisplayName("Invalid email format")
        void registerIllegalArgument2Test(String invalidEmail) {
            validUserToCreateForm.replace("email",invalidEmail);
            ResponseEntity<AppUserDto>  validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        registerAbsoluteURL,
                        RequestEntity.post(SecurityConstants.Endpoint.REGISTER)
                                .body(validUserToCreateForm),
                        AppUserDto.class
                );

            } catch (HttpClientErrorException e) {

                String responseBodyAsString = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    resultError = objectMapper.readValue(responseBodyAsString, ErrorResponseBodyDto.class);
                } catch (JsonProcessingException e2) {
                    // resultError is left as null to be handled with the first assert.
                }

            }

            assertNull(validResult, "The method call was interrupted, so the normal result is null.");
            assertNotNull(resultError, "Exception launched, caught by CustomResponseEntityExceptionHandler, error response obtained and mapped");
            assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(resultError.getStatus()), "Response status is BAD_REQUEST");
            assertEquals("Invalid email format", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Email already taken")
        void registerIllegalArgument3Test() {
            validUserToCreateForm.replace("email", SecurityConstants.TEST_USER_USERNAME);

            ResponseEntity<AppUserDto>  validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        registerAbsoluteURL,
                        RequestEntity.post(SecurityConstants.Endpoint.REGISTER)
                                .body(validUserToCreateForm),
                        AppUserDto.class
                );

            } catch (HttpClientErrorException e) {

                String responseBodyAsString = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    resultError = objectMapper.readValue(responseBodyAsString, ErrorResponseBodyDto.class);
                } catch (JsonProcessingException e2) {
                    // resultError is left as null to be handled with the first assert.
                }

            }

            assertNull(validResult, "The method call was interrupted, so the normal result is null.");
            assertNotNull(resultError, "Exception launched, caught by CustomResponseEntityExceptionHandler, error response obtained and mapped");
            assertEquals(HttpStatus.CONFLICT.value(), Integer.parseInt(resultError.getStatus()), "Response status is CONFLICT");
            assertEquals("Email already taken", resultError.getMessage(), "Error message is correct");
        }

    }

    @Nested
    class LoginTest {

        @Test
        @DisplayName("Valid case")
        void loginTest() {
            ResponseEntity<Map<String, String>> result = restTemplate.exchange(
                    loginAbsoluteURL,
                    HttpMethod.POST,
                    RequestEntity.post(SecurityConstants.Endpoint.LOGIN)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .body(validUserToLoginForm),
                    new ParameterizedTypeReference<Map<String, String>>() {}
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertNotNull(result.getBody().get(SecurityConstants.ACCESS_TOKEN_PARAM_NAME), "Access token was returned");

            String accessToken = result.getBody().get(SecurityConstants.ACCESS_TOKEN_PARAM_NAME);
            JwtService jwtService = new JwtServiceImpl();
            assertDoesNotThrow(
                    () -> jwtService.verify(accessToken),
                    "Returned access token is valid."
            );
        }

        @Test
        @DisplayName("Mandatory attribute missing")
        void loginIllegalArgumentTest() {
            validUserToLoginForm.remove("username");
            assertNull(validUserToLoginForm.get("username"), "Username attribute removed from the user to create.");

            ResponseEntity<Map<String, String>>  validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        loginAbsoluteURL,
                        HttpMethod.POST,
                        RequestEntity.post(SecurityConstants.Endpoint.LOGIN)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(validUserToLoginForm),
                        new ParameterizedTypeReference<Map<String, String>>() {}
                );

            } catch (HttpClientErrorException e) {

                String responseBodyAsString = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    resultError = objectMapper.readValue(responseBodyAsString, ErrorResponseBodyDto.class);
                } catch (JsonProcessingException e2) {
                    // resultError is left as null to be handled with the first assert.
                }

            }

            assertNull(validResult, "The method call was interrupted, so the normal result is null.");
            assertNotNull(resultError, "Exception launched, caught by CustomResponseEntityExceptionHandler, error response obtained and mapped");
            assertEquals(HttpStatus.FORBIDDEN.value(), Integer.parseInt(resultError.getStatus()), "Response status is FORBIDDEN");
            assertEquals("The request does not contain the username parameter", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Email doesn't exist")
        void loginIllegalArgument2Test() {
            validUserToLoginForm.replace("username", Arrays.asList("unknown@alkemygmail.com"));

            ResponseEntity<Map<String, String>>  validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        loginAbsoluteURL,
                        HttpMethod.POST,
                        RequestEntity.post(SecurityConstants.Endpoint.LOGIN)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(validUserToLoginForm),
                        new ParameterizedTypeReference<Map<String, String>>() {}
                );

            } catch (HttpClientErrorException e) {

                String responseBodyAsString = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    resultError = objectMapper.readValue(responseBodyAsString, ErrorResponseBodyDto.class);
                } catch (JsonProcessingException e2) {
                    // resultError is left as null to be handled with the first assert.
                }

            }

            assertNull(validResult, "The method call was interrupted, so the normal result is null.");
            assertNotNull(resultError, "Exception launched, caught by CustomResponseEntityExceptionHandler, error response obtained and mapped");
            assertEquals(HttpStatus.FORBIDDEN.value(), Integer.parseInt(resultError.getStatus()), "Response status is FORBIDDEN");
            assertEquals("Bad credentials", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Incorrect password")
        void loginIllegalArgument3Test() {
            validUserToLoginForm.replace("password", Arrays.asList("wrongpassword"));

            ResponseEntity<Map<String, String>>  validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        loginAbsoluteURL,
                        HttpMethod.POST,
                        RequestEntity.post(SecurityConstants.Endpoint.LOGIN)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(validUserToLoginForm),
                        new ParameterizedTypeReference<Map<String, String>>() {}
                );

            } catch (HttpClientErrorException e) {

                String responseBodyAsString = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    resultError = objectMapper.readValue(responseBodyAsString, ErrorResponseBodyDto.class);
                } catch (JsonProcessingException e2) {
                    // resultError is left as null to be handled with the first assert.
                }

            }

            assertNull(validResult, "The method call was interrupted, so the normal result is null.");
            assertNotNull(resultError, "Exception launched, caught by CustomResponseEntityExceptionHandler, error response obtained and mapped");
            assertEquals(HttpStatus.FORBIDDEN.value(), Integer.parseInt(resultError.getStatus()), "Response status is FORBIDDEN");
            assertEquals("Bad credentials", resultError.getMessage(), "Error message is correct");
        }

    }

    static List<String> getInvalidEmails() {
        return Arrays.asList(
                //twoAtSigns
                "emailaddress@something@somethingelse.com",
                //noAtSign
                "emailaddresssomethingelse.com",
                //noPoint
                "emailaddress@something",
                //noFirstPart
                "@somethingelse.com",
                //illegalChars
                "something#%$&#%$/&@somethingelse.com"
        );
    }

}