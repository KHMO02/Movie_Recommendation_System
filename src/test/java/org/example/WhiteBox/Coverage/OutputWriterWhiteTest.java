package org.example.WhiteBox.Coverage;

import org.example.io.output.OutputWriter;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.recommendation.UserRecommendations;
import org.example.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OutputWriterWhiteTest {

    @Test
    void CoverageTest() throws ValidationException, IOException {

        // Arrange
        Movie m1 = new Movie("Zodiac", "Z001", List.of("Thriller"));
        Movie m2 = new Movie("Prisoners", "P002", List.of("Thriller"));
        Movie m3 = new Movie("Sherlock", "S003", List.of("Thriller"));

        User user1 = new User("Khaled", "12345678A", List.of("Z001"));
        User user2 = new User("Mahmoud", "12345658B", List.of("Z001"));


        Recommender recommender = new Recommender(List.of(m1, m2, m3));
        List<User> users = new ArrayList<>();

        users.add(user1);
        users.add(user2);
        List<UserRecommendations> result = recommender.recommendForAllUsers(users);

        OutputWriter writer = new OutputWriter();

        // Act
        writer.writeRecommendations(result);

        // Assert (file exists & content written)
        File file = new File("recommendations.txt");
        assertTrue(file.exists());

        String content = Files.readString(file.toPath());
        assertTrue(content.contains("Khaled"));
        assertTrue(content.contains("Prisoners"));
    }
}
