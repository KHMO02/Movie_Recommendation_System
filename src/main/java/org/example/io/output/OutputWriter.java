package org.example.io.output;

import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.UserRecommendations;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Writes recommendation results to file.
 */
public class OutputWriter
{

    private static final String OUTPUT_FILE = "recommendations.txt";

    public void writeRecommendations(List<UserRecommendations> allRecommendations) throws IOException
    {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE))
        {
            for (int i = 0; i < allRecommendations.size(); i++)
            {
                writeUserBlock(writer, allRecommendations.get(i));
                if (i < allRecommendations.size() - 1)
                    writer.write('\n');
            }
        }
        openFile();
    }

    public void writeError(String message)
    {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE))
        {
            writer.write("ERROR: " + message);
        }
        catch (IOException e)
        {
            System.err.println("Failed to write error: " + e.getMessage());
        }
    }

    private void writeUserBlock(FileWriter writer, UserRecommendations userRec) throws IOException
    {
        User user = userRec.user();
        writer.write(user.getUserName() + ", " + user.getUserId());
        writer.write('\n');

        writeMovieList(writer, userRec.recommendations());
    }

    private void writeMovieList(FileWriter writer, List<Movie> movies) throws IOException
    {
        for (int i = 0; i < movies.size(); i++)
        {
            writer.write(movies.get(i).getTitle());
            if (i < movies.size() - 1)
                writer.write(", ");
        }
    }

    private void openFile()
    {
        try
        {
            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().open(new File(OUTPUT_FILE));
        }
        catch (IOException e)
        {
            System.err.println("Could not open file: " + e.getMessage());
        }
    }
}