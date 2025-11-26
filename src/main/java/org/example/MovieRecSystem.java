package org.example;

import org.example.exceptions.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieRecSystem {

    private final List<Movie>   moviesList  = new ArrayList<>();
    private final List<User>    usersList   = new ArrayList<>();
    private final Loader        loader      = new Loader();
    private final Recommender   recommender = new Recommender();

    public static void main(String[] args) {
        MovieRecSystem app = new MovieRecSystem();
        try {
            app.moviesList.addAll(app.loader.loadMovies());
            app.usersList.addAll(app.loader.loadUsers());
            app.recommender.writeRecommendations(app.usersList, app.moviesList);
        } catch (ValidationException | IOException e) {
            app.recommender.writeErrorMsg(e.getMessage());
        }
    }
}
