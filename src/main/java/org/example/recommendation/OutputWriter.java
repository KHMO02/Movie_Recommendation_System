package org.example.recommendation;

import org.example.model.Movie;
import org.example.model.User;

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
        System.out.println("DEBUG: Writing to: " + new File(OUTPUT_FILE).getAbsolutePath());
        System.out.println("DEBUG: Number of users: " + allRecommendations.size());

        try (FileWriter writer = new FileWriter(OUTPUT_FILE))
        {
            for (int i = 0; i < allRecommendations.size(); i++)
            {
                writeUserBlock(writer, allRecommendations.get(i));
                if (i < allRecommendations.size() - 1)
                    writer.write('\n');
            }
        }

        System.out.println("DEBUG: File written successfully!");
        openFile();
    }

    public void writeError(String message)
    {
        System.err.println("ERROR: " + message);
        try (FileWriter writer = new FileWriter(OUTPUT_FILE))
        {
            writer.write("ERROR: " + message);
        }
        catch (IOException e)
        {
            System.err.println("Failed to write error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void writeUserBlock(FileWriter writer, UserRecommendations userRec) throws IOException
    {
        User user = userRec.getUser();
        System.out.println("DEBUG: Writing recommendations for: " + user.getUserName());

        writer.write(user.getUserName() + ", " + user.getUserId());
        writer.write('\n');

        writeMovieList(writer, userRec.getRecommendations());
    }

    private void writeMovieList(FileWriter writer, List<Movie> movies) throws IOException
    {
        System.out.println("DEBUG: Number of recommendations: " + movies.size());

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