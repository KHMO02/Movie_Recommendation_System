package org.example.io.loader;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;

import org.example.io.loader.component.FileReader;
import org.example.io.loader.component.IdValidator;
import org.example.io.loader.component.ParsedEntity;
import org.example.io.loader.component.TwoLineParser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for loading entities using Template Method Pattern
 */
public abstract class EntityLoader<T>
{
    protected final FileReader fileReader;
    protected final TwoLineParser parser;
    protected final IdValidator idValidator;

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