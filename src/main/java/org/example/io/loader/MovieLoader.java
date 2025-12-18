package org.example.io.loader;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;
import org.example.io.loader.component.ParsedEntity;
import org.example.model.Movie;

/**
 * Concrete loader for Movie entities
 */
public class MovieLoader extends EntityLoader<Movie>
{

    @Override
    protected String getPromptMessage()
    {
        return "Enter movies' file name/path: ";
    }

    @Override
    protected ErrorMessage getFormatErrorMessage()
    {
        return ErrorMessage.FILE_MOVIES_FORMAT;
    }

    @Override
    protected ErrorMessage getIdNotUniqueError()
    {
        return ErrorMessage.MOVIE_ID_NOT_UNIQUE;
    }

    @Override
    protected Movie createEntity(ParsedEntity parsed) throws ValidationException
    {
        return new Movie(parsed.getName(), parsed.getId(), parsed.getAttributes());
    }
}