package org.example;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Recommender {

    public List<Movie> recommendForUser(User user, List<Movie> allMovies) {
        List<String> likedIds = user.getMoviesId();
        Set<String> likedIdSet = new HashSet<>(likedIds);

        // 1) collect genres the user likes
        Set<String> likedGenres = new HashSet<>();
        for (Movie movie : allMovies) {
            if (likedIdSet.contains(movie.getId())) {
                likedGenres.addAll(movie.getGenre());
            }
        }

        // 2) collect movies in those genres, but not already liked
        List<Movie> recommendations = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (likedIdSet.contains(movie.getId())) {
                continue;
            }
            for (String genre : movie.getGenre()) {
                if (likedGenres.contains(genre)) {
                    recommendations.add(movie);
                    break;
                }
            }
        }

        return recommendations;
    }

    public void writeRecommendations(List<User> users, List<Movie> movies) throws IOException {
        try (FileWriter writer = new FileWriter("recommendations.txt")) {
            for (int u = 0; u < users.size(); u++) {
                User user = users.get(u);
                writeUserBlock(writer, user, movies);
                if (u < users.size() - 1) {
                    writer.write('\n');
                }
            }
        }
        openOutputFile();
    }

    private void writeUserBlock(FileWriter writer, User user, List<Movie> movies) throws IOException {
        writer.write(user.getUserName() + ", " + user.getUserId());
        writer.write('\n');

        List<Movie> recs = recommendForUser(user, movies);
        for (int i = 0; i < recs.size(); i++) {
            writer.write(recs.get(i).getTitle());
            if (i < recs.size() - 1) {
                writer.write(", ");
            }
        }
    }

    public void writeErrorMsg(String message) {
        try (FileWriter writer = new FileWriter("recommendations.txt")) {
            writer.write(message);
        } catch (IOException e) {
            System.out.println("An error occurred while writing error file.");
            e.printStackTrace();
        }
    }

    private void openOutputFile() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File("recommendations.txt"));
            } else {
                System.out.println("Desktop is not supported on this system.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while opening recommendations.txt.");
            e.printStackTrace();
        }
    }
}
