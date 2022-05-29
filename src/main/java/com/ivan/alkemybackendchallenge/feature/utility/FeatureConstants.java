package com.ivan.alkemybackendchallenge.feature.utility;

public class FeatureConstants {

    // ENTITY ATTRIBUTES
    public static final Integer CHARACTER_NAME_LENGTH = 80;
    public static final Integer CHARACTER_HISTORY_LENGTH = 80000;
    public static final Integer GENRE_NAME_LENGTH = 80;
    public static final Integer MEDIA_WORK_TITLE_LENGTH = 80;
    public static final String DEFAULT_IMAGE_URL = "https://defaultimageurl.com";
    public static final Integer IMAGE_URL_LENGTH = 255;
    public static final Integer MEDIA_WORK_SCORE_MIN_VALUE = 1;
    public static final Integer MEDIA_WORK_SCORE_MAX_VALUE = 5;

    public static class Endpoint {

        public static final String MEDIA_CHARACTERS = "/characters";
        public static final String MEDIA_GENRE = "/genres";
        public static final String MEDIA_WORK = "/movies";

    }

}
