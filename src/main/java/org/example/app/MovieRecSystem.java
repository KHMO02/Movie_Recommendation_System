package org.example.app;

import org.example.exception.ValidationException;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.UserLoader;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.io.output.OutputWriter;
import org.example.recommendation.UserRecommendations;

import java.io.IOException;
import java.util.List;

public class MovieRecSystem
{

    private final MovieLoader  movieLoader;
    private final UserLoader   userLoader;
    private final OutputWriter writer;

    public MovieRecSystem(MovieLoader movieLoader, UserLoader userLoader, OutputWriter writer)
    {
        this.movieLoader = movieLoader;
        this.userLoader  = userLoader;
        this.writer      = writer;
    }

    public MovieRecSystem()
    {
        this(new MovieLoader(), new UserLoader(), new OutputWriter());
    }

    public void run() throws ValidationException, IOException
    {
        List<Movie> moviesList  = movieLoader.load();
        List<User>  usersList   = userLoader.load();
        Recommender recommender = new Recommender(moviesList);
        List<UserRecommendations> recommendations = recommender.recommendForAllUsers(usersList);
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
            System.err.println("Error: " + e.getMessage());
            app.writer.writeError(e.getMessage());
        }
    }
}