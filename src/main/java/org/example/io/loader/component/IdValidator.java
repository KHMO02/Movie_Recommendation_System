package org.example.io.loader.component;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates ID uniqueness
 */
public class IdValidator
{

    public void validateAllUnique(List<ParsedEntity> entities, ErrorMessage error) throws ValidationException
    {
        Set<String> seen = new HashSet<>();
        for (ParsedEntity entity : entities)
            if (!seen.add(entity.getId()))
                throw new ValidationException(error, entity.getName()); // Throw immediately when duplicate found
    }
}