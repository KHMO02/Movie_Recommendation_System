package org.example.recommendation;

import org.example.model.Movie;
import org.example.model.User;

import java.util.*;

/**
 * Generates movie recommendations based on user preferences.
 */
public class Recommender
{

    public List<Movie> recommendForUser(User user, List<Movie> allMovies)
    {
        Set<String> likedMovieIds = new HashSet<>(user.getMoviesId());
        Set<String> likedGenres = collectLikedGenres(allMovies, likedMovieIds);

        return findMatchingMovies(allMovies, likedMovieIds, likedGenres);
    }

    public List<UserRecommendations> recommendForAllUsers(List<User> users, List<Movie> allMovies)
    {
        List<UserRecommendations> allRecommendations = new ArrayList<>();
        for (User user : users)
        {
            List<Movie> recommendations = recommendForUser(user, allMovies);
            allRecommendations.add(new UserRecommendations(user, recommendations));
        }
        return allRecommendations;
    }

    private Set<String> collectLikedGenres(List<Movie> allMovies, Set<String> likedMovieIds)
    {
        Set<String> genres = new HashSet<>();
        for (Movie movie : allMovies)
        {
            if (likedMovieIds.contains(movie.getId()))
                genres.addAll(movie.getGenre());
        }
        return genres;
    }

    private List<Movie> findMatchingMovies(List<Movie> allMovies, Set<String> likedMovieIds, Set<String> likedGenres)
    {

        List<Movie> recommendations = new ArrayList<>();
        for (Movie movie : allMovies)
        {
            if (likedMovieIds.contains(movie.getId()))
                continue;

            if (hasMatchingGenre(movie, likedGenres))
                recommendations.add(movie);
        }
        return recommendations;
    }

    private boolean hasMatchingGenre(Movie movie, Set<String> likedGenres)
    {
        for (String genre : movie.getGenre())
        {
            if (likedGenres.contains(genre))
                return true;
        }

        return false;
    }
}