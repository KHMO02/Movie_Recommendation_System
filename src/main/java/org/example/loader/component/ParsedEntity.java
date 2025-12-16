package org.example.loader.component;

import java.util.List;

/**
 * Represents a parsed entity before conversion to domain model
 */
public class ParsedEntity
{
    private final String name;
    private final String id;
    private final List<String> attributes;

    public ParsedEntity(String name, String id, List<String> attributes)
    {
        this.name = name;
        this.id = id;
        this.attributes = attributes;
    }

    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }

    public List<String> getAttributes()
    {
        return attributes;
    }
}