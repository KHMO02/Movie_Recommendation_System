package org.example.model;

import org.example.validation.FormatValidator;
import org.example.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private final String        name;
    private final String        id;
    private final List<String>  moviesId; //movie IDs


    public User(String name, String id, List<String> moviesId) throws ValidationException
    {
        FormatValidator.validateUserName(name);
        FormatValidator.validateUserId(id);
        FormatValidator.validateMoviesId(moviesId);

        this.name       = name;
        this.id         = id;
        this.moviesId   = new ArrayList<>(moviesId);
    }

    public String getUserName() {
        return name;
    }

    public String getUserId() {
        return id;
    }

    public List<String> getMoviesId() {
        return List.copyOf(moviesId);
    }

}
