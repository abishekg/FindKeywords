package com.abishekgoutham;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static Map<String, String> readPredefinedWords(String predefinedWordsFilePath) {
        Map<String, String> predefinedWords = new LinkedHashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(predefinedWordsFilePath))) {
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                predefinedWords.put(word.trim().toLowerCase(), word.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException Error reading predefinedWordsFilePath: " + e);
        } catch (IOException e) {
            System.out.println("IOException Error reading predefinedWordsFilePath: " + e);
        }

        return predefinedWords;
    }

    public static Map<String, Long> readTextFile(String inputFilePath, Map<String, String> predefinedWords) {
        Map<String, Long> matches = new HashMap<>();

        for (String key : predefinedWords.keySet()) {
            matches.put(predefinedWords.get(key), 0L);
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(" ");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i].toLowerCase();
                    for (String key : predefinedWords.keySet()) {
                        String[] keyWords = key.split(" ");
                        if (doesMatch(words, i, keyWords)) {
                            String originalWord = predefinedWords.get(key);
                            matches.put(originalWord, matches.get(originalWord) + 1);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException Error reading inputFilePath: " + e);
        } catch (IOException e) {
            System.out.println("IOException Error reading inputFilePath: " + e);
        }

        return matches;
    }

    private static boolean doesMatch(String[] words, int index, String[] phrase) {
        if (index + phrase.length > words.length) {
            return false;
        }
        for (int i = 0; i < phrase.length; i++) {
            if (!words[index + i].equalsIgnoreCase(phrase[i])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java com.abishekgoutham.Main <inputFilePath> <predefinedWordsFilePath>");
            return;
        }

        String inputFile = args[0];
        String predefinedWordsFile = args[1];

        Instant startInstant = Instant.now();

        Map<String, String> predefinedWords = readPredefinedWords(predefinedWordsFile);
        Map<String, Long> matchCounts = readTextFile(inputFile, predefinedWords);

        System.out.println("----------------------------------------------");
        System.out.printf("%-28s %-15s%n", "Predefined Words", "Match Counts");
        System.out.println("----------------------------------------------");
        for (String key : predefinedWords.keySet()) {
            System.out.printf("%-28s %-15d%n", predefinedWords.get(key), matchCounts.get(predefinedWords.get(key)));
        }

        Instant endInstant = Instant.now();

        ZonedDateTime startTime = startInstant.atZone(ZoneId.systemDefault());
        ZonedDateTime endTime = endInstant.atZone(ZoneId.systemDefault());
        long totalTimeMillis = Duration.between(startInstant, endInstant).toMillis();
        long seconds = totalTimeMillis / 1000;
        long milliseconds = totalTimeMillis % 1000;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS z");

        System.out.println("----------------------------------------------");
        System.out.println("Start time       : " + startTime.format(formatter));
        System.out.println("End time         : " + endTime.format(formatter));
        System.out.println("Total Time Taken : " + seconds + " seconds " + milliseconds + " milliseconds");
    }
}
