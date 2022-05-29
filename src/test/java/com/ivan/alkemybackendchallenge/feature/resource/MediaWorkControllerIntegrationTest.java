package com.ivan.alkemybackendchallenge.feature.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaWorkDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.ReducedMediaWorkDto;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class MediaWorkControllerIntegrationTest {

    private final RestTemplate restTemplate;
    private final String absoluteURL = "http://localhost:8080/movies";
    private final String MOCK_AUTH_HEADER_NAME = "Authorization";
    private final String MOCK_AUTH_HEADER_VALUE;
    private Map<String, String> existingMediaWorkForm;
    private Map<String, Object> validMediaWorkToCreateForm;

    @Autowired
    MediaWorkControllerIntegrationTest(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        String token = new JwtServiceImpl().createAccessToken(
                SecurityConstants.TEST_USER_USERNAME,
                Arrays.asList(SecurityConstants.DEFAULT_USER_ROLE),
                "http://localhost:8080/auth/login"
        );
        this.MOCK_AUTH_HEADER_VALUE = "Bearer " + token;
    }

    /**
     * Generates a map with the valid attributes of a new media work that doesn't currently exist in the database.
     *
     * The keys have the same names as the attributes from the class MediaWorkDto.
     */
    @BeforeEach
    private void initValidMediaWorkToCreateForm() {
        this.validMediaWorkToCreateForm = new HashMap<>();
        this.validMediaWorkToCreateForm.put("mediaWorkType", "tv_series");
        this.validMediaWorkToCreateForm.put("title", "House of Mouse");
        this.validMediaWorkToCreateForm.put("imageUrl", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Disney%27s_House_of_Mouse_logo.svg/250px-Disney%27s_House_of_Mouse_logo.svg.png");
        this.validMediaWorkToCreateForm.put("releaseDate", "2001-01-13");
        this.validMediaWorkToCreateForm.put("score", "2.5");

        Map<String, String> associatedExistingCharacter = new HashMap<>();
        associatedExistingCharacter.put("id", "1");
        associatedExistingCharacter.put("name", "Mickey Mouse");
        associatedExistingCharacter.put("imageUrl", "https://upload.wikimedia.org/wikipedia/en/thumb/d/d4/Mickey_Mouse.png/220px-Mickey_Mouse.png");
        associatedExistingCharacter.put("age", "93");
        associatedExistingCharacter.put("bodyWeight", "10");
        associatedExistingCharacter.put("history", "Mickey Mouse is an American animated cartoon character co-created in 1928 by Walt Disney, who originally voiced the character, and Ub Iwerks. The longtime mascot of The Walt Disney Company, Mickey is an anthropomorphic mouse who typically wears red shorts, large yellow shoes, and white gloves.");

        Map<String, String> associatedNewCharacter = new HashMap<>();
        associatedNewCharacter.put("name", "Donald Duck");
        associatedNewCharacter.put("imageUrl", "https://upload.wikimedia.org/wikipedia/en/thumb/a/a5/Donald_Duck_angry_transparent_background.png/220px-Donald_Duck_angry_transparent_background.png");
        associatedNewCharacter.put("age", "86");
        associatedNewCharacter.put("bodyWeight", "20.0");
        associatedNewCharacter.put("history", "Donald Fauntleroy Duck is a cartoon character created by The Walt Disney Company.");

        List<Map<String, String>> characterList = new ArrayList<>();
        characterList.add(associatedExistingCharacter);
        characterList.add(associatedNewCharacter);
        this.validMediaWorkToCreateForm.put("mediaCharacters", characterList);

        Map<String, String> associatedExistingGenre = new HashMap<>();
        associatedExistingGenre.put("id", "2");
        associatedExistingGenre.put("name", "Animation");

        Map<String, String> associatedNewGenre = new HashMap<>();
        associatedNewGenre.put("name", "Crossover");

        List<Map<String, String>> genreList = new ArrayList<>();
        genreList.add(associatedExistingGenre);
        genreList.add(associatedNewGenre);
        this.validMediaWorkToCreateForm.put("mediaGenres", genreList);
    }

    /**
     * Generates a map with the attributes of a media work that currently exist in the database.
     *
     * The keys have the same names as the attributes from the class MediaWorkDto.
     */
    @BeforeEach
    private void initExistingMediaWorkForm() {
        this.existingMediaWorkForm = new HashMap<>();
        this.existingMediaWorkForm.put("id", "1");
        this.existingMediaWorkForm.put("mediaWorkType", "movie");
        this.existingMediaWorkForm.put("title", "The Prince and the Pauper");
        this.existingMediaWorkForm.put("imageUrl", "https://upload.wikimedia.org/wikipedia/en/thumb/b/bb/Disney%27s_The_Prince_and_the_Pauper_%281990%29.jpg/220px-Disney%27s_The_Prince_and_the_Pauper_%281990%29.jpg");
        this.existingMediaWorkForm.put("releaseDate", "1990-11-16");
        this.existingMediaWorkForm.put("score", "3.5");
    }

    @Nested
    class CreateMediaWorkTest {

        @Test
        @DisplayName("Valid creation case")
        void createMediaWorkTest() {
            ResponseEntity<MediaWorkDto> result = restTemplate.postForEntity(
                    absoluteURL,
                    RequestEntity.post(FeatureConstants.Endpoint.MEDIA_WORK)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .body(validMediaWorkToCreateForm),
                    MediaWorkDto.class
            );
            assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value(), "Response status is CREATED");
            assertNotNull(result.getBody(), "Response body is present");

            String expectedTitle = ((String) validMediaWorkToCreateForm.get("title")).toLowerCase();
            assertEquals(expectedTitle, result.getBody().getTitle(), "Entity with the same title was stored and returned");

            assertNull(validMediaWorkToCreateForm.get("id"), "id attribute wasn't sent in the request.");
            assertNotNull(result.getBody().getId(), "Id attribute is present in the response (generated by the server)");
        }

        @Test
        @DisplayName("Illegal attribute")
        void createMediaCharacterIllegalParamTest() {

            validMediaWorkToCreateForm.replace("releaseDate", "THIS IS NOT A DATE");

            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        absoluteURL,
                        RequestEntity.post(FeatureConstants.Endpoint.MEDIA_WORK)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(validMediaWorkToCreateForm),
                        MediaWorkDto.class
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
            assertEquals("Invalid release date format. The date format must be yyyy-mm-dd", resultError.getMessage(), "Error message is correct");

        }

        @Test
        @DisplayName("Mandatory body attribute missing")
        void createMediaCharacterMissingAttributeTest() {

            validMediaWorkToCreateForm.remove("title");

            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        absoluteURL,
                        RequestEntity.post(FeatureConstants.Endpoint.MEDIA_WORK)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(validMediaWorkToCreateForm),
                        MediaWorkDto.class
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
            assertEquals("Media work title not provided", resultError.getMessage(), "Error message is correct");

        }

    }

    @Nested
    @Order(1)
    class GetMediaWorkTest {

        @Test
        @DisplayName("Full param search")
        void getMediaWorkFullParamTest() {
            Map<String, String> params = new HashMap<>();
            params.put("name", "prince");
            params.put("genre", "1");
            params.put("order", "asc");

            ResponseEntity<List<ReducedMediaWorkDto>> result = restTemplate.exchange(
                    absoluteURL,
                    HttpMethod.GET,
                    RequestEntity.get(FeatureConstants.Endpoint.MEDIA_WORK)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .build(),
                    new ParameterizedTypeReference<List<ReducedMediaWorkDto>>() {},
                    params
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertFalse(result.getBody().isEmpty(), "Response body is not empty");
            assertEquals("the prince and the pauper", result.getBody().get(0).getTitle(), "Media work title found is the expected one");
        }

        @Test
        @DisplayName("Partial param search")
        void getMediaWorkPartialParamTest() {
            Map<String, String> params = new HashMap<>();
            params.put("genre", "1");

            ResponseEntity<List<ReducedMediaWorkDto>> result = restTemplate.exchange(
                    absoluteURL,
                    HttpMethod.GET,
                    RequestEntity.get(FeatureConstants.Endpoint.MEDIA_WORK)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .build(),
                    new ParameterizedTypeReference<List<ReducedMediaWorkDto>>() {},
                    params
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertFalse(result.getBody().isEmpty(), "Response body is not empty");
            assertEquals(2, result.getBody().size(), "Correct number of media works found with blanc name and genre id=1");
        }

        @Test
        @DisplayName("No params search")
        void getMediaWorkNoParamTest() {
            ResponseEntity<List<ReducedMediaWorkDto>> result = restTemplate.exchange(
                    absoluteURL,
                    HttpMethod.GET,
                    RequestEntity.get(FeatureConstants.Endpoint.MEDIA_WORK)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .build(),
                    new ParameterizedTypeReference<List<ReducedMediaWorkDto>>() {}
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertFalse(result.getBody().isEmpty(), "Response body is not empty");
            assertEquals(2, result.getBody().size(), "Correct number of media works found with no args");
        }

    }

    @Nested
    class UpdateMediaWorkTest {

        @Test
        @DisplayName("Valid case")
        void updateMediaWorkTest() {
            String oldTitle = existingMediaWorkForm.get("title");
            String updatedTitle = "(Updated) The Prince and the Pauper";
            existingMediaWorkForm.replace("title", updatedTitle);

            ResponseEntity<MediaWorkDto> result = restTemplate.exchange(
                    absoluteURL,
                    HttpMethod.PUT,
                    RequestEntity.put(FeatureConstants.Endpoint.MEDIA_WORK)
                            .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                            .body(existingMediaWorkForm),
                    MediaWorkDto.class
            );

            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertEquals(Long.parseLong(existingMediaWorkForm.get("id")), result.getBody().getId(), "Entity with the same id was stored and returned");
            assertEquals(updatedTitle.toLowerCase(), result.getBody().getTitle(), "Returned title was the updated one");
        }

        @Test
        @DisplayName("Param type mismatch")
        void updateMediaWorkIllegalParamTest() {
            // param type mismatch: double =/= String
            existingMediaWorkForm.replace("score", "NOT A NUMBER");

            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL,
                        HttpMethod.PUT,
                        RequestEntity.put(FeatureConstants.Endpoint.MEDIA_WORK)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(existingMediaWorkForm),
                        MediaWorkDto.class
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
        void updateMediaWorkMissingParamTest() {
            //*************** ID NOT SENT
            existingMediaWorkForm.remove("id");

            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL,
                        HttpMethod.PUT,
                        RequestEntity.put(FeatureConstants.Endpoint.MEDIA_WORK)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(existingMediaWorkForm),
                        MediaWorkDto.class
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

            assertNull(existingMediaWorkForm.get("id"), "id attribute wasn't sent in the request.");
            assertNull(validResult, "The method call was interrupted, so the normal result is null.");
            assertNotNull(resultError, "Exception launched, caught by CustomResponseEntityExceptionHandler, error response obtained and mapped");
            assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(resultError.getStatus()), "Response status is BAD_REQUEST");
            assertEquals("Media work id must be provided", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Media work not found")
        void updateMediaWorkNotFoundTest() {
            //*************** NON EXISTENT ID
            existingMediaWorkForm.replace("id", "1234");

            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL,
                        HttpMethod.PUT,
                        RequestEntity.put(FeatureConstants.Endpoint.MEDIA_WORK)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .body(existingMediaWorkForm),
                        MediaWorkDto.class
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
            assertEquals("Media work not found", resultError.getMessage(), "Error message is correct");
        }

    }

    @Nested
    class DeleteMediaWorkTest {

        @Test
        @DisplayName("Valid case")
        void deleteMediaWorkTest() {

            final int MEDIA_WORK_TO_DEL_ID = 2;
            ResponseEntity<Void> result;
            try {
                result = restTemplate.exchange(
                        absoluteURL + "/" + MEDIA_WORK_TO_DEL_ID,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_WORK)
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
        @DisplayName("Media work not found")
        void deleteMediaWorkNotFoundTest() {
            final int MEDIA_WORK_TO_DEL_ID = 123456;
            ResponseEntity<Void> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL + "/" + MEDIA_WORK_TO_DEL_ID,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_WORK)
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
            assertEquals("Media work not found", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Path type mismatch")
        void deleteMediaWorkIllegalPathTest() {
            final String MEDIA_WORK_TO_DEL_ID = "THIS IS NOT A NUMBER";
            ResponseEntity<Void> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL + "/" + MEDIA_WORK_TO_DEL_ID,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_WORK)
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

    @Nested
    class AddCharacterToMediaWorkTest {

        @Test
        @DisplayName("Valid case")
        void addCharacterToMediaWorkTest() {
            final String MEDIA_WORK_ID_PATH_NAME = "idMovie";
            final Long MEDIA_WORK_ID_PATH_VALUE = 2L;
            final String CHARACTER_ID_PATH_NAME = "idCharacter";
            final Long CHARACTER_ID_PATH_VALUE = 2L;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(MEDIA_WORK_ID_PATH_NAME, MEDIA_WORK_ID_PATH_VALUE.toString());
            uriParams.put(CHARACTER_ID_PATH_NAME, CHARACTER_ID_PATH_VALUE.toString());
            String pathVariable = "/{"+ MEDIA_WORK_ID_PATH_NAME +"}/characters/{"+ CHARACTER_ID_PATH_NAME +"}";
            ResponseEntity<MediaWorkDto> result;
            try {
                result = restTemplate.postForEntity(
                        absoluteURL + pathVariable,
                        RequestEntity.post(FeatureConstants.Endpoint.MEDIA_WORK + pathVariable, uriParams)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        MediaWorkDto.class,
                        uriParams
                );
            } catch (HttpClientErrorException e) {
                result = ResponseEntity.notFound().build();
            }
            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertEquals(MEDIA_WORK_ID_PATH_VALUE, result.getBody().getId(), "Entity with the same id was returned");
            boolean characterFoundInMediaWorkCharactersList = result.getBody().getMediaCharacters()
                            .stream()
                            .anyMatch(character -> character.getId().equals(CHARACTER_ID_PATH_VALUE));
            assertTrue(characterFoundInMediaWorkCharactersList, "Character is present in the media work's list of characters.");
        }

        @Test
        @DisplayName("Media work not found")
        void addCharacterToMediaWorkNotFoundWorkTest() {
            final String MEDIA_WORK_ID_PATH_NAME = "idMovie";
            //***********NON-EXISTING MEDIA WORK ID
            final Long MEDIA_WORK_ID_PATH_VALUE = 1234L;
            final String CHARACTER_ID_PATH_NAME = "idCharacter";
            final Long CHARACTER_ID_PATH_VALUE = 2L;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(MEDIA_WORK_ID_PATH_NAME, MEDIA_WORK_ID_PATH_VALUE.toString());
            uriParams.put(CHARACTER_ID_PATH_NAME, CHARACTER_ID_PATH_VALUE.toString());
            String pathVariable = "/{"+ MEDIA_WORK_ID_PATH_NAME +"}/characters/{"+ CHARACTER_ID_PATH_NAME +"}";
            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        absoluteURL + pathVariable,
                        RequestEntity.post(FeatureConstants.Endpoint.MEDIA_WORK + pathVariable, uriParams)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        MediaWorkDto.class,
                        uriParams
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
            assertEquals("Media work not found", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Character not found")
        void addCharacterToMediaWorkNotFoundCharTest() {
            final String MEDIA_WORK_ID_PATH_NAME = "idMovie";
            final Long MEDIA_WORK_ID_PATH_VALUE = 2L;
            final String CHARACTER_ID_PATH_NAME = "idCharacter";
            //***********NON-EXISTING character ID
            final Long CHARACTER_ID_PATH_VALUE = 1234L;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(MEDIA_WORK_ID_PATH_NAME, MEDIA_WORK_ID_PATH_VALUE.toString());
            uriParams.put(CHARACTER_ID_PATH_NAME, CHARACTER_ID_PATH_VALUE.toString());
            String pathVariable = "/{"+ MEDIA_WORK_ID_PATH_NAME +"}/characters/{"+ CHARACTER_ID_PATH_NAME +"}";
            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        absoluteURL + pathVariable,
                        RequestEntity.post(FeatureConstants.Endpoint.MEDIA_WORK + pathVariable, uriParams)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        MediaWorkDto.class,
                        uriParams
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
        void addCharacterToMediaWorkIllegalPathTest() {
            final String MEDIA_WORK_ID_PATH_NAME = "idMovie";
            // ************* ID TYPE MISMATCH LONG =/= STRING
            final String MEDIA_WORK_ID_PATH_VALUE = "NOT A NUMBER";
            final String CHARACTER_ID_PATH_NAME = "idCharacter";
            final Long CHARACTER_ID_PATH_VALUE = 2L;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(MEDIA_WORK_ID_PATH_NAME, MEDIA_WORK_ID_PATH_VALUE);
            uriParams.put(CHARACTER_ID_PATH_NAME, CHARACTER_ID_PATH_VALUE.toString());
            String pathVariable = "/{"+ MEDIA_WORK_ID_PATH_NAME +"}/characters/{"+ CHARACTER_ID_PATH_NAME +"}";
            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.postForEntity(
                        absoluteURL + pathVariable,
                        RequestEntity.post(FeatureConstants.Endpoint.MEDIA_WORK + pathVariable, uriParams)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        MediaWorkDto.class,
                        uriParams
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

    @Nested
    class RemoveCharacterFromMediaWorkTest {

        @Test
        @DisplayName("Valid case")
        void removeCharacterFromMediaWorkTest() {
            final String MEDIA_WORK_ID_PATH_NAME = "idMovie";
            final Long MEDIA_WORK_ID_PATH_VALUE = 1L;
            final String CHARACTER_ID_PATH_NAME = "idCharacter";
            final Long CHARACTER_ID_PATH_VALUE = 3L;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(MEDIA_WORK_ID_PATH_NAME, MEDIA_WORK_ID_PATH_VALUE.toString());
            uriParams.put(CHARACTER_ID_PATH_NAME, CHARACTER_ID_PATH_VALUE.toString());
            String pathVariable = "/{"+ MEDIA_WORK_ID_PATH_NAME +"}/characters/{"+ CHARACTER_ID_PATH_NAME +"}";
            ResponseEntity<MediaWorkDto> result;
            try {
                result = restTemplate.exchange(
                        absoluteURL + pathVariable,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_WORK + pathVariable, uriParams)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        MediaWorkDto.class,
                        uriParams
                );
            } catch (HttpClientErrorException e) {
                result = ResponseEntity.notFound().build();
            }
            assertEquals(HttpStatus.OK.value(), result.getStatusCode().value(), "Response status is OK");
            assertNotNull(result.getBody(), "Response body is present");
            assertEquals(MEDIA_WORK_ID_PATH_VALUE, result.getBody().getId(), "Entity with the same id was returned");
            boolean characterNotFoundInMediaWorkCharactersList = result.getBody().getMediaCharacters()
                    .stream()
                    .noneMatch(character -> character.getId().equals(CHARACTER_ID_PATH_VALUE));
            assertTrue(characterNotFoundInMediaWorkCharactersList, "Character is not present in the media work's list of characters.");
        }

        @Test
        @DisplayName("Media work not found")
        void removeCharacterFromMediaWorkNotFoundWorkTest() {
            final String MEDIA_WORK_ID_PATH_NAME = "idMovie";
            //***********NON-EXISTING MEDIA WORK ID
            final Long MEDIA_WORK_ID_PATH_VALUE = 1234L;
            final String CHARACTER_ID_PATH_NAME = "idCharacter";
            final Long CHARACTER_ID_PATH_VALUE = 1L;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(MEDIA_WORK_ID_PATH_NAME, MEDIA_WORK_ID_PATH_VALUE.toString());
            uriParams.put(CHARACTER_ID_PATH_NAME, CHARACTER_ID_PATH_VALUE.toString());
            String pathVariable = "/{"+ MEDIA_WORK_ID_PATH_NAME +"}/characters/{"+ CHARACTER_ID_PATH_NAME +"}";
            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL + pathVariable,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_WORK + pathVariable, uriParams)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        MediaWorkDto.class,
                        uriParams
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
            assertEquals("Media work not found", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Character not currently associated with media work")
        void removeCharacterFromMediaWorkNotFoundCharTest() {
            final String MEDIA_WORK_ID_PATH_NAME = "idMovie";
            final Long MEDIA_WORK_ID_PATH_VALUE = 1L;
            final String CHARACTER_ID_PATH_NAME = "idCharacter";
            //***********NON-EXISTING character ID
            final Long CHARACTER_ID_PATH_VALUE = 1234L;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(MEDIA_WORK_ID_PATH_NAME, MEDIA_WORK_ID_PATH_VALUE.toString());
            uriParams.put(CHARACTER_ID_PATH_NAME, CHARACTER_ID_PATH_VALUE.toString());
            String pathVariable = "/{"+ MEDIA_WORK_ID_PATH_NAME +"}/characters/{"+ CHARACTER_ID_PATH_NAME +"}";
            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL + pathVariable,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_WORK + pathVariable, uriParams)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        MediaWorkDto.class,
                        uriParams
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
            assertEquals("The character is not currently associated with the media work", resultError.getMessage(), "Error message is correct");
        }

        @Test
        @DisplayName("Path type mismatch")
        void removeCharacterFromMediaWorkIllegalPathTest() {
            final String MEDIA_WORK_ID_PATH_NAME = "idMovie";
            // ************* ID TYPE MISMATCH LONG =/= STRING
            final String MEDIA_WORK_ID_PATH_VALUE = "NOT A NUMBER";
            final String CHARACTER_ID_PATH_NAME = "idCharacter";
            final Long CHARACTER_ID_PATH_VALUE = 1L;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(MEDIA_WORK_ID_PATH_NAME, MEDIA_WORK_ID_PATH_VALUE);
            uriParams.put(CHARACTER_ID_PATH_NAME, CHARACTER_ID_PATH_VALUE.toString());
            String pathVariable = "/{"+ MEDIA_WORK_ID_PATH_NAME +"}/characters/{"+ CHARACTER_ID_PATH_NAME +"}";
            ResponseEntity<MediaWorkDto> validResult = null;
            ErrorResponseBodyDto resultError = null;

            try {

                validResult = restTemplate.exchange(
                        absoluteURL + pathVariable,
                        HttpMethod.DELETE,
                        RequestEntity.delete(FeatureConstants.Endpoint.MEDIA_WORK + pathVariable, uriParams)
                                .header(MOCK_AUTH_HEADER_NAME, MOCK_AUTH_HEADER_VALUE)
                                .build(),
                        MediaWorkDto.class,
                        uriParams
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