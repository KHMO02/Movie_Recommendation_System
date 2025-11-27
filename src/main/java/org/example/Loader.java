package org.example;

import org.example.exceptions.ValidationException;
import org.example.exceptions.ValidationException.ErrorMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Loader {

    public List<Movie> loadMovies() throws IOException, ValidationException {
        List<String> lines = readLinesFromUser("Enter movies' file name/path: ");
        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 2) {
            String[] firstLine = splitHeaderLine(lines, i, ErrorMessage.FILE_MOVIES_FORMAT);
            String title = firstLine[0];
            String id = firstLine[1];

            String[] secondLine = lines.get(i + 1).split(",\\s*");
            List<String> genres = Arrays.asList(secondLine);
            movies.add(new Movie(title, id, genres));
        }

        return movies;
    }

    public List<User> loadUsers() throws IOException, ValidationException {
        List<String> lines = readLinesFromUser("Enter users' file name/path: ");
        List<User> users = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 2) {
            String[] firstLine = splitHeaderLine(lines, i, ErrorMessage.FILE_USERS_FORMAT);
            String name = firstLine[0];
            String id = firstLine[1];

            String[] secondLine = lines.get(i + 1).split(",\\s*");
            List<String> movieIds = Arrays.asList(secondLine);
            users.add(new User(name, id, movieIds));
        }

        return users;
    }

    private String[] splitHeaderLine(List<String> lines,
                                     int index,
                                     ErrorMessage errorMessage) throws ValidationException {
        if (index + 1 >= lines.size()) {
            throw new ValidationException(errorMessage);
        }

        String[] parts = lines.get(index).split(",\\s*");
        if (parts.length != 2) {
            throw new ValidationException(errorMessage);
        }

        return parts;
    }

    private List<String> readLinesFromUser(String prompt) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println(prompt);
        String fileName = scan.nextLine();

        List<String> lines = new ArrayList<>();
        try (BufferedReader bufread = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufread.readLine()) != null) {
                if (!line.isBlank()) {
                    lines.add(line.trim());
                }
            }
        }

        return lines;
    }
}
