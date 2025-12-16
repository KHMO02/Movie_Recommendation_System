package org.example.loader.component;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * Validates ID uniqueness
 */
public class IdValidator
{
    private final Set<String> seenIds = new HashSet<>();

    public void validateUnique(String id, String name, ErrorMessage error) throws ValidationException
    {
        if (!seenIds.add(id))
            throw new ValidationException(error, name);
    }

    public void reset()
    {
        seenIds.clear();
    }
}