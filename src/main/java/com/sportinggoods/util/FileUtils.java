package com.sportinggoods.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * Initializes a file with headers if it doesn't exist.
     *
     * @param filePath The path to the file.
     * @param header   The header line to write.
     */
    public static void initializeFile(String filePath, String header) {
        File file = new File(filePath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(header);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error initializing file: " + filePath);
            }
        }
    }

    /**
     * Reads all lines from a file, excluding the header.
     *
     * @param filePath The path to the file.
     * @return A list of strings representing each line.
     */
    public static List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }
        return lines;
    }

    /**
     * Appends a line to a file.
     *
     * @param filePath The path to the file.
     * @param line     The line to append.
     * @return True if successful, false otherwise.
     */
    public static boolean appendToFile(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to file: " + filePath);
            return false;
        }
    }

    /**
     * Writes all lines to a file, overwriting existing content.
     *
     * @param filePath The path to the file.
     * @param lines    The list of lines to write.
     * @return True if successful, false otherwise.
     */
    public static boolean writeAllLines(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            return false;
        }
    }
}