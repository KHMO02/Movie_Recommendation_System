package org.example.unit.io.output;

import org.example.exception.ValidationException;
import org.example.io.output.OutputWriter;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class OuputWriterTest {
    @Test
    public void OutputWritertest() throws ValidationException, IOException {
        OutputWriter outputWriter = new OutputWriter();
        List<UserRecommendations> recommendations = List.of(
                new UserRecommendations(new User("John", "123456789", List.of("S001")),
                        List.of(new Movie("Superman", "S001", List.of("Action")))),
                new UserRecommendations(new User("Ali", "12345678k", List.of("M001")),
                        List.of(new Movie("Scream", "S002", List.of("Horror"))))
        );
        outputWriter.writeRecommendations(recommendations);
        Path output = Path.of("recommendations.txt");
        List<String> expected = Files.readAllLines(output);

        assertTrue(expected.get(0).contains("John"));
        assertTrue(expected.get(0).contains("123456789"));
        assertTrue(expected.get(1).contains("Superman"));
        assertTrue(expected.get(2).contains("Ali"));
        assertTrue(expected.get(2).contains("12345678k"));
        assertTrue(expected.get(3).contains("Scream"));
        Files.deleteIfExists(output);
    }
}
