package com.ivan.alkemybackendchallenge.security.dto.data;

import java.time.LocalDateTime;

public class ErrorResponseBodyDto {

    /**
     * The time the error occurred. Default: now.
     */
    private String timestamp;
    /**
     * The numeric status code of the response.
     */
    private String status;
    /**
     * The reason phrase of the response status.
     */
    private String error;
    /**
     * The description of the source of the error.
     */
    private String message;
    /**
     * The endpoint the request landed on.
     */
    private String path;

    public ErrorResponseBodyDto() {
        this.timestamp = LocalDateTime.now().toString();
    }

    /**
     * Full args constructor.
     *
     * @param timestamp the time the error occurred.
     * @param status the numeric status code of the response.
     * @param error the reason phrase of the response status.
     * @param message the description of the source of the error.
     * @param path the endpoint the request landed on.
     */
    public ErrorResponseBodyDto(String timestamp, String status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Full args constructor without timestamp.
     *
     * The timestamp is generated automatically with the current time.
     *
     * @param status the numeric status code of the response.
     * @param error the reason phrase of the response status.
     * @param message the description of the source of the error.
     * @param path the endpoint the request landed on.
     */
    public ErrorResponseBodyDto(String status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now().toString();
    }


    /**
     * {@link ErrorResponseBodyDto#timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * {@link ErrorResponseBodyDto#timestamp
     */
    public ErrorResponseBodyDto setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * {@link ErrorResponseBodyDto#status
     */
    public String getStatus() {
        return status;
    }

    /**
     * {@link ErrorResponseBodyDto#status
     */
    public ErrorResponseBodyDto setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * {@link ErrorResponseBodyDto#error
     */
    public String getError() {
        return error;
    }

    /**
     * {@link ErrorResponseBodyDto#error
     */
    public ErrorResponseBodyDto setError(String error) {
        this.error = error;
        return this;
    }

    /**
     * {@link ErrorResponseBodyDto#message
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link ErrorResponseBodyDto#message
     */
    public ErrorResponseBodyDto setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * {@link ErrorResponseBodyDto#path
     */
    public String getPath() {
        return path;
    }

    /**
     * {@link ErrorResponseBodyDto#path
     */
    public ErrorResponseBodyDto setPath(String path) {
        this.path = path;
        return this;
    }

}