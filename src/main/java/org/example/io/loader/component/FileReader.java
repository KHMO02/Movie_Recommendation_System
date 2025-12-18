package org.example.io.loader.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles reading file input from user and file system
 */
public class FileReader
{
    private final Scanner scanner;

    public FileReader()
    {
        this.scanner = new Scanner(System.in);
    }

    public FileReader(Scanner scanner)
    {
        this.scanner = scanner;
    }

    public List<String> readLines(String prompt) throws IOException
    {
        System.out.println(prompt);
        String fileName = scanner.nextLine();

        List<String> lines = new ArrayList<>();
        try (BufferedReader bufread = new BufferedReader(new java.io.FileReader(fileName)))
        {
            String line;
            while ((line = bufread.readLine()) != null)
            {
                if (!line.isBlank())
                    lines.add(line.trim());
            }
        }

        return lines;
    }
}