package com.ivan.alkemybackendchallenge.security.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.alkemybackendchallenge.security.dto.data.ErrorResponseBodyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomHttpServletResponseUtility {

    public static void addTokenToResponseBody(String accessToken, HttpServletResponse response)
            throws IOException {

        Map<String, String> tokensMap = new HashMap<>();
        tokensMap.put(SecurityConstants.ACCESS_TOKEN_PARAM_NAME, accessToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokensMap);
    }

    public static void addErrorToResponseBody(HttpServletRequest request, HttpServletResponse response,
                                              String errorMessage, HttpStatus httpStatus) throws IOException {

        ErrorResponseBodyDto responseBody = new ErrorResponseBodyDto(
                LocalDateTime.now().toString(),
                Integer.toString(httpStatus.value()),
                httpStatus.getReasonPhrase(),
                errorMessage,
                request.getServletPath()
        );
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

    public static ResponseEntity<Object> buildErrorResponse(HttpStatus httpStatus, String message, String requestURI) {
        ErrorResponseBodyDto responseBody = new ErrorResponseBodyDto(
                LocalDateTime.now().toString(),
                Integer.toString(httpStatus.value()),
                httpStatus.getReasonPhrase(),
                trimDefaultMessage(message),
                requestURI
        );
        return ResponseEntity.status(httpStatus).body(responseBody);
    }

    private static String trimDefaultMessage(String message) {
        String trimmedMessage;
        Integer substringEndIndex = message.indexOf(":");
        if (substringEndIndex < 0) {
            trimmedMessage = message;
        } else {
            trimmedMessage = message.substring(0,  substringEndIndex );
        }
        return trimmedMessage;
    }

}
