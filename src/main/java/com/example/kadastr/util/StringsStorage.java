package com.example.kadastr.util;

//stores string constants for reusability and optimization of memory allocation
public class StringsStorage {

    public static final String AUTH_CONTROLLER_URL = "/api/auth";
    public static final String AUTH_CONTROLLER_LOGIN_URL = "/login";
    public static final String AUTH_CONTROLLER_REGISTRATION_URL = "/registration";
    public static final String COMMENTS_CONTROLLER_URL = "/api/comments";
    public static final String NEWS_CONTROLLER_URL = "/api/news";
    public static final String CREATED_STATUS = "CREATED";
    public static final String UPDATED_STATUS = "UPDATED";
    public static final String DELETED_STATUS = "DELETED";
    public static final String USERNAME_VALIDATOR_MESSAGE = "Username must be between 3 and 40 symbols!";
    public static final String PASSWORD_VALIDATOR_MESSAGE = "Password must be between 8 and 80 symbols!";
    public static final String NAME_VALIDATOR_MESSAGE = "Name must be between 2 and 20 symbols!";
    public static final String SURNAME_VALIDATOR_MESSAGE = "Surname must be between 2 and 20 symbols!";
    public static final String PARENT_NAME_VALIDATOR_MESSAGE = "Parent name must be between 2 and 20 symbols!";
    public static final String COMMENT_TEXT_VALIDATOR_MESSAGE = "Comment must be lower than 300 symbols and not empty!";
    public static final String NEWS_TEXT_VALIDATOR_MESSAGE = "Text must be lower than 2000 symbols and not empty!";
    public static final String NEWS_TITLE_VALIDATOR_MESSAGE = "Title must be lower than 150 symbols and not empty!";
    public static final String ROLE_VALIDATOR_MESSAGE = "Role can be only from list - [ROLE_ADMIN, ROLE_JOURNALIST, ROLE_SUBSCRIBER]!";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

}
