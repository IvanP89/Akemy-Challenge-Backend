package com.ivan.alkemybackendchallenge.feature.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaCharacterDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.ReducedMediaCharacterDto;
import com.ivan.alkemybackendchallenge.feature.utility.FeatureConstants;
import com.ivan.alkemybackendchallenge.security.dto.data.ErrorResponseBodyDto;
import com.ivan.alkemybackendchallenge.security.service.JwtServiceImpl;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MediaCharacterControllerIntegrationTest {

    private final RestTemplate restTemplate;
    private final String absoluteURL = "http://localhost:8080/characters";
    private final String MOCK_AUTH_HEADER_NAME = "Authorization";
    private final String MOCK_AUTH_HEADER_VALUE;
    private Map<String, String> existingCharacterForm;
    private Map<String, String> validCharacterToCreateForm;

    @Autowired
    public MediaCharacterControllerIntegrationTest(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        String token = new JwtServiceImpl().createAccessToken(
                SecurityConstants.TEST_USER_USERNAME,
                Arrays.asList(SecurityConstants.DEFAULT_USER_ROLE),
                "http://localhost:8080/auth/login"
        );
        this.MOCK_AUTH_HEADER_VALUE = "Bearer " + token;
    }

    /**
     * Generates a map with the attributes of a character that currently exist in the database.
     *
     * The keys have the same names as the attributes from the class MediaCharacterDto.
     */
    @BeforeEach
    private void initExistingCharacterForm() {
        this.existingCharacterForm = new HashMap<>();
        this.existingCharacterForm.put("id", "1");
        this.existingCharacterForm.put("name", "Mickey Mouse");
        this.existingCharacterForm.put("imageUrl", "https://upload.wikimedia.org/wikipedia/en/thumb/d/d4/Mickey_Mouse.png/220px-Mickey_Mouse.png");
        this.existingCharacterForm.put("age", "93");
        this.existingCharacterForm.put("bodyWeight", "10");
        this.existingCharacterForm.put("history", "Mickey Mouse is an American animated cartoon character co-created in 1928 by Walt Disney, who originally voiced the character, and Ub Iwerks. The longtime mascot of The Walt Disney Company, Mickey is an anthropomorphic mouse who typically wears red shorts, large yellow shoes, and white gloves.");
    }

    /**
     * Generates a map with the valid attributes of a new character that doesn't currently exist in the database.
     *
     * The keys have the same names as the attributes from the class MediaCharacterDto.
     */
    @BeforeEach
    private void initValidCharacterToCreateForm() {
        this.validCharacterToCreateForm = new HashMap<>();
        this.validCharacterToCreateForm.put("name", "Princess Jasmine");
        this.validCharacterToCreateForm.put("imageUrl", "https://upload.wikimedia.org/wikipedia/en/thumb/7/71/Princess_Jasmine_disney.png/220px-Princess_Jasmine_disney.png");
        this.validCharacterToCreateForm.put("age", "15");
        this.validCharacterToCreateForm.put("bodyWeight", "50.5");
        this.validCharacterToCreateForm.put("history", "Princess Jasmine is a fictional character who appears in Walt Disney Pictures' 31st animated feature film Aladdin (1992).");
    }

    @Nested
    class CreateMediaCharacterTest {

        @Test
        @DisplayName("Valid creation case")
        void createMediaCharacterTest() {
            ResponseEntity<MediaCharacterDto> result = restTemplate.postForEntity(
                    absoluteURL,
                    RequestEntity.post(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .body(validCharacterToCreateForm),
                    MediaCharacterDto.class
            );
            assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value(), "Response status is CREATED");
            assertNotNull(result.getBody(), "Response body is present");
            assertEquals(validCharacterToCreateForm.get("name").toLowerCase(), result.getBody().getName(), "Entity with the same name was stored and returned");
            assertNull(validCharacterToCreateForm.get("id"), "id attribute wasn't sent in the request.");
            assertNotNull(result.getBody().getId(), "Id attribute is present in the response (generated by the server)");
        }

        @Test
        @DisplayName("Param type mismatch")
        void createMediaCharacterIllegalParamTest() {

            validCharacterToCreateForm.replace("age", "THIS IS NOT A NUMBER");

            ResponseEntity<MediaCharacterDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        absoluteURL,
                        RequestEntity.post(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(validCharacterToCreateForm),
                        MediaCharacterDto.class
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
            assertEquals("JSON parse error", resultError.getMessage(), "Error message is correct");

        }

    }

    @Nested
    class GetMediaCharacterTest {

        @Test
        @DisplayName("Full param search")
        void getMediaCharacterFullParamTest() {
            Map<String, String> params = new HashMap<>();
            params.put("name", "mickey");
            params.put("age", "93");
            params.put("movies", "1");

            ResponseEntity<List<ReducedMediaCharacterDto>> result = restTemplate.exchange(
                    absoluteURL,
                    HttpMethod.GET,
                    RequestEntity.get(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .build(),
                    new ParameterizedTypeReference<List<ReducedMediaCharacterDto>>() {},
                    params
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertFalse(result.getBody().isEmpty(), "Response body is not empty");
            assertEquals("mickey mouse", result.getBody().get(0).getName(), "Character name result");
        }

        @Test
        @DisplayName("Partial param search")
        void getMediaCharacterPartialParamTest() {
            Map<String, String> params = new HashMap<>();
            params.put("movies", "1");

            ResponseEntity<List<ReducedMediaCharacterDto>> result = restTemplate.exchange(
                    absoluteURL,
                    HttpMethod.GET,
                    RequestEntity.get(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .build(),
                    new ParameterizedTypeReference<List<ReducedMediaCharacterDto>>() {},
                    params
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertFalse(result.getBody().isEmpty(), "Response body is not empty");
            assertEquals(2, result.getBody().size(), "Correct number of characters found with criteria: name param not provided, age param not provided, media id=1");
        }

        @Test
        @DisplayName("No param search")
        void getMediaCharacterNoParamTest() {
            ResponseEntity<List<ReducedMediaCharacterDto>> result = restTemplate.exchange(
                    absoluteURL,
                    HttpMethod.GET,
                    RequestEntity.get(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .build(),
                    new ParameterizedTypeReference<List<ReducedMediaCharacterDto>>() {}
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertFalse(result.getBody().isEmpty(), "Response body is not empty");
            assertEquals(2, result.getBody().size(), "Correct number of characters found with no criteria");
        }

    }

    @Nested
    class UpdateMediaCharacterTest {

        @Test
        @DisplayName("Valid case")
        void updateMediaCharacterTest() {

            existingCharacterForm.replace("history", "Making some changes in this field.");

            ResponseEntity<MediaCharacterDto> result = restTemplate.exchange(
                    absoluteURL,
                    HttpMethod.PUT,
                    RequestEntity.put(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .body(existingCharacterForm),
                    MediaCharacterDto.class
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertEquals(existingCharacterForm.get("name").toLowerCase(), result.getBody().getName(), "Entity with the same name was stored and returned");
            assertEquals(existingCharacterForm.get("history"), result.getBody().getHistory(), "Entity attribute was updated");

        }

        @Test
        @DisplayName("Param type mismatch")
        void updateMediaCharacterIllegalParamTest() {
            // param type mismatch: int =/= String
            existingCharacterForm.replace("age", "NOT A NUMBER");

            ResponseEntity<MediaCharacterDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL,
                        HttpMethod.PUT,
                        RequestEntity.put(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(existingCharacterForm),
                        MediaCharacterDto.class
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
            assertEquals("JSON parse error", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Mandatory entity attribute missing")
        void updateMediaCharacterMissingParamTest() {

            //*************** ID NOT SENT
            existingCharacterForm.remove("id");

            ResponseEntity<MediaCharacterDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL,
                        HttpMethod.PUT,
                        RequestEntity.put(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(existingCharacterForm),
                        MediaCharacterDto.class
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

            assertNull(existingCharacterForm.get("id"), "id attribute wasn't sent in the request.");
            assertNull(validResult, "The method call was interrupted, so the normal result is null.");
            assertNotNull(resultError, "Exception launched, caught by CustomResponseEntityExceptionHandler, error response obtained and mapped");
            assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(resultError.getStatus()), "Response status is BAD_REQUEST");
            assertEquals("Character id must be provided", resultError.getMessage(), "Error message is correct");

        }

        @Test
        @DisplayName("Character not found")
        void updateMediaCharacterNotFoundTest() {

            //*************** NON EXISTENT ID
            existingCharacterForm.replace("id", "1234");

            ResponseEntity<MediaCharacterDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL,
                        HttpMethod.PUT,
                        RequestEntity.put(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(existingCharacterForm),
                        MediaCharacterDto.class
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
            assertEquals(HttpStatus.NOT_FOUND.value(), Integer.parseInt(resultError.getStatus()), "Response status is NOT_FOUND");
            assertEquals("Character not found", resultError.getMessage(), "Error message is correct");

        }

    }

    @Nested
    class DeleteMediaCharacterTest {

        @Test
        @DisplayName("Valid case")
        void deleteMediaCharacterTest() {

            final int CHARACTER_ID = 2;
            ResponseEntity<Void> result;
            try {
                result = restTemplate.exchange(
                        absoluteURL + "/" + CHARACTER_ID,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        Void.class
                );
            } catch (HttpClientErrorException e) {
                result = ResponseEntity.notFound().build();
            }
            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");

        }

        @Test
        @DisplayName("Character not found")
        void deleteMediaCharacterNotFoundTest() {

            final int CHARACTER_ID = 123456;
            ResponseEntity<Void> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL + "/" + CHARACTER_ID,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        Void.class
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
            assertEquals(HttpStatus.NOT_FOUND.value(), Integer.parseInt(resultError.getStatus()), "Response status is NOT_FOUND");
            assertEquals("Character not found", resultError.getMessage(), "Error message is correct");

        }

        @Test
        @DisplayName("Path type mismatch")
        void deleteMediaCharacterIllegalPathTest() {

            final String CHARACTER_ID = "THIS IS NOT A NUMBER";
            ResponseEntity<Void> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL + "/" + CHARACTER_ID,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_CHARACTERS)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        Void.class
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
            assertEquals("param/arg type mismatch", resultError.getMessage(), "Error message is correct");

        }

    }

}