package org.example.loader;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;

import org.example.loader.component.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for loading entities using Template Method Pattern
 */
public abstract class EntityLoader<T>
{
    private final FileReader fileReader;
    private final TwoLineParser parser;
    private final IdValidator idValidator;

    // Default constructor with default dependencies
    public EntityLoader()
    {
        this(new FileReader(), new TwoLineParser(), new IdValidator());
    }

    // Constructor for dependency injection for testing
    public EntityLoader(FileReader fileReader, TwoLineParser parser, IdValidator idValidator)
    {
        this.fileReader = fileReader;
        this.parser = parser;
        this.idValidator = idValidator;
    }

    public List<T> load() throws IOException, ValidationException
    {
        List<String> lines = fileReader.readLines(getPromptMessage());
        List<ParsedEntity> parsedEntities = parser.parse(lines, getFormatErrorMessage());

        idValidator.validateAllUnique(parsedEntities, getIdNotUniqueError());

        List<T> entities = new ArrayList<>();
        for (ParsedEntity parsed : parsedEntities)
            entities.add(createEntity(parsed));

        return entities;
    }

    // Abstract methods for subclasses
    protected abstract String getPromptMessage();

    protected abstract ErrorMessage getFormatErrorMessage();

    protected abstract ErrorMessage getIdNotUniqueError();

    protected abstract T createEntity(ParsedEntity parsed) throws ValidationException;
}