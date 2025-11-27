package org.example;

import org.example.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private final String        id;
    private final String        title;
    private final List<String>  genre;

    public Movie(String title, String id, List<String> genre) throws ValidationException
    {
        FormatValidator.validateMovieTitle(title);
        FormatValidator.validateMovieId(id);
        FormatValidator.validateMovieIdMatchesTitle(id,title);

        this.title  = title;
        this.id     = id;
        this.genre  = new ArrayList<>(genre);
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public List<String> getGenre() {
        return List.copyOf(genre);
    }

}