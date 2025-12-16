package org.example.validation;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;

import java.util.List;
import java.util.regex.Pattern;

// Validator for Movie and User format validation using compiled regex patterns
public final class FormatValidator
{

    // Compiled patterns for better performance
    private static final Pattern MOVIE_TITLE_PATTERN =
            Pattern.compile("([A-Z][a-z]*)(-[A-Z][a-z]*)*(\\s([A-Z][a-z]*)(-[A-Z][a-z]*)*)*");

    private static final Pattern MOVIE_ID_PATTERN =
            Pattern.compile("[A-Z]+\\d{3}");

    private static final Pattern USER_NAME_PATTERN =
            Pattern.compile("[A-Za-z]+(?:\\s[A-Za-z]+)*");

    private static final Pattern USER_ID_PATTERN =
            Pattern.compile("\\d{9}|\\d{8}[A-Za-z]");

    // Private constructor to prevent instantiation
    private FormatValidator()
    {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Validates movie title format: each word starts with uppercase followed by lowercase
    // Example: "The Lord Of The Rings"
    public static void validateMovieTitle(String title) throws ValidationException
    {
        if (title == null || title.isBlank())
            throw new ValidationException(ErrorMessage.MOVIE_TITLE, title);

        if (!MOVIE_TITLE_PATTERN.matcher(title).matches())
            throw new ValidationException(ErrorMessage.MOVIE_TITLE, title);
    }

    // Validates movie ID format: uppercase letters followed by 3 digits
    // Example: "TLO123" or "LOTR456"
    public static void validateMovieId(String id) throws ValidationException
    {
        if (id == null || id.isBlank())
            throw new ValidationException(ErrorMessage.MOVIE_ID_LETTERS, id);

        if (!MOVIE_ID_PATTERN.matcher(id).matches())
            throw new ValidationException(ErrorMessage.MOVIE_ID_LETTERS, id);
    }

    // Validates that movie ID letters match the first letters of title words
    // Example: "The Lord Of" matches "TLO123"
    public static void validateMovieIdMatchesTitle(String id, String title) throws ValidationException
    {
        var words = title.split("\\s+");
        var idLetters = id.replaceAll("\\d+$", "");

        if (idLetters.length() != words.length)
            throw new ValidationException(ErrorMessage.MOVIE_ID_LETTERS, id);

        for (int i = 0; i < words.length; i++)
        {
            if (words[i].charAt(0) != idLetters.charAt(i))
                throw new ValidationException(ErrorMessage.MOVIE_ID_LETTERS, id);
        }
    }

    // Validates a list of movie IDs
    public static void validateMoviesId(List<String> ids) throws ValidationException
    {
        if (ids == null || ids.isEmpty())
            throw new ValidationException(ErrorMessage.MOVIE_ID_LETTERS, "null or empty");

        for (String id : ids)
            validateMovieId(id);
    }

    // Validates username format: letters and single spaces, no leading/trailing spaces
    // Example: "John Doe" or "Mary"
    public static void validateUserName(String name) throws ValidationException
    {
        if (name == null || name.isBlank())
            throw new ValidationException(ErrorMessage.USER_NAME, name);

        if (!USER_NAME_PATTERN.matcher(name).matches())
            throw new ValidationException(ErrorMessage.USER_NAME, name);
    }

    // Validates user ID format: 9 digits OR 8 digits followed by a letter
    // Example: "123456789" or "12345678A"
    public static void validateUserId(String id) throws ValidationException
    {
        if (id == null || id.isBlank())
            throw new ValidationException(ErrorMessage.USER_ID, id);

        if (!USER_ID_PATTERN.matcher(id).matches())
            throw new ValidationException(ErrorMessage.USER_ID, id);
    }
}