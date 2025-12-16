package org.example;

import org.example.exception.ValidationException;
import org.example.loader.*;
import org.example.model.*;
import org.example.recommendation.*;

import java.io.IOException;
import java.util.List;

public class MovieRecSystem
{

    private final MovieLoader   movieLoader;
    private final UserLoader    userLoader;
    private final Recommender   recommender;
    private final OutputWriter  writer;

    private List<Movie> moviesList;
    private List<User>  usersList;

    public MovieRecSystem(MovieLoader movieLoader, UserLoader userLoader, Recommender recommender, OutputWriter writer)
    {
        this.movieLoader    = movieLoader;
        this.userLoader     = userLoader;
        this.recommender    = recommender;
        this.writer         = writer;
    }

    public MovieRecSystem()
    {
        this(new MovieLoader(), new UserLoader(), new Recommender(), new OutputWriter());
    }

    public void initialize() throws ValidationException, IOException
    {
        moviesList = movieLoader.load();
        usersList  = userLoader.load();
    }

    public void run() throws ValidationException, IOException
    {
        initialize();
        List<UserRecommendations> recommendations = recommender.recommendForAllUsers(usersList, moviesList);
        writer.writeRecommendations(recommendations);
    }

    public static void main(String[] args)
    {
        MovieRecSystem app = new MovieRecSystem();
        try
        {
            app.run();
        }
        catch (ValidationException | IOException e)
        {
            app.writer.writeError(e.getMessage());
        }
    }
}