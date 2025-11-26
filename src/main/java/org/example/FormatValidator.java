package org.example;

import org.example.exceptions.ValidationException;
import org.example.exceptions.ValidationException.ErrorMessage;

import java.util.List;

public class FormatValidator {


    public static void validateMovieTitle(String title) throws ValidationException {
        if (!title.matches("([A-Z][a-z]*\\s?)+"))
            throw new ValidationException(ErrorMessage.MOVIE_TITLE, title);

    }

    public static void validateMovieId(String id) throws ValidationException {
        if (!id.matches("[A-Z]+\\d{3}"))
            throw new ValidationException(ErrorMessage.MOVIE_ID_LETTERS, id);

    }

    public static void validateMoviesId(List<String> ids) throws ValidationException {
        for (String id : ids)
            validateMovieId(id);
    }

    // First char letter, then letters or spaces (no leading space)
    public static void validateUserName(String name) throws ValidationException {
        if (!name.matches("[A-Za-z][A-Za-z ]*"))
            throw new ValidationException(ErrorMessage.USER_NAME, name);

    }

    public static void validateUserId(String id) throws ValidationException {
        if (!id.matches("\\d{9}|\\d{8}[A-Za-z]"))
            throw new ValidationException(ErrorMessage.USER_ID, id);

    }
}
