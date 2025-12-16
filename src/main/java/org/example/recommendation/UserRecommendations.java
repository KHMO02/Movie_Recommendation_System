package org.example.recommendation;

import org.example.model.Movie;
import org.example.model.User;

import java.util.List;

/**
 * Simple container for a user and their recommendations.
 */
public class UserRecommendations
{

    private final User user;
    private final List<Movie> recommendations;

    public UserRecommendations(User user, List<Movie> recommendations)
    {
        this.user = user;
        this.recommendations = recommendations;
    }

    public User getUser()
    {
        return user;
    }

    public List<Movie> getRecommendations()
    {
        return recommendations;
    }
}