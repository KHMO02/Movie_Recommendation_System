package org.example.recommendation;

import org.example.model.Movie;
import org.example.model.User;

import java.util.List;

/**
 * Simple container for a user and their recommendations.
 */
public record UserRecommendations(User user, List<Movie> recommendations) {}