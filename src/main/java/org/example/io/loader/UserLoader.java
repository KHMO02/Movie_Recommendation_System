package org.example.io.loader;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;
import org.example.io.loader.component.ParsedEntity;
import org.example.model.User;

/**
 * Concrete loader for User entities
 */
public class UserLoader extends EntityLoader<User>
{

    @Override
    protected String getPromptMessage()
    {
        return "Enter users' file name/path: ";
    }

    @Override
    protected ErrorMessage getFormatErrorMessage()
    {
        return ErrorMessage.FILE_USERS_FORMAT;
    }

    @Override
    protected ErrorMessage getIdNotUniqueError()
    {
        return ErrorMessage.USER_ID_NOT_UNIQUE;
    }

    @Override
    protected User createEntity(ParsedEntity parsed) throws ValidationException
    {
        return new User(parsed.getName(), parsed.getId(), parsed.getAttributes());
    }
}