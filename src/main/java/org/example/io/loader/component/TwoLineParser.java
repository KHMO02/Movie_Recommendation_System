package org.example.io.loader.component;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parses two-line format: "Name, ID" followed by "attr1, attr2, ..."
 */
public class TwoLineParser
{

    public List<ParsedEntity> parse(List<String> lines, ErrorMessage formatError) throws ValidationException
    {
        List<ParsedEntity> entities = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 2)
        {
            String[] headerParts = splitHeaderLine(lines, i, formatError);
            String name = headerParts[0];
            String id = headerParts[1];

            String[] attributeLine = lines.get(i + 1).split(",\\s*");
            List<String> attributes = Arrays.asList(attributeLine);

            entities.add(new ParsedEntity(name, id, attributes));
        }

        return entities;
    }

    private String[] splitHeaderLine(List<String> lines, int index, ErrorMessage error) throws ValidationException
    {
        if (index + 1 >= lines.size())
            throw new ValidationException(error);

        String[] parts = lines.get(index).split(",\\s*");
        if (parts.length != 2)
            throw new ValidationException(error);

        return parts;
    }
}