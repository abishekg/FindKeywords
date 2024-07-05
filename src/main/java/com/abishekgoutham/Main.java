package com.abishekgoutham;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static Map<String, String> readPredefinedWords(String predefinedWordsFilePath) {
        Map<String, String> predefinedWordsMap = new LinkedHashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(predefinedWordsFilePath))) {
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                predefinedWordsMap.put(word.trim().toLowerCase(), word.trim());
            }
        } catch (IOException e) {
            System.out.println("IOException Error reading predefinedWordsFilePath: " + e);
        }

        return predefinedWordsMap;
    }

    public static Map<String, Long> mergeResults(List<Map<String, Long>> results) {
        Map<String, Long> mergedMap = new HashMap<>();

        for (Map<String, Long> resultMap : results) {
            for (Map.Entry<String, Long> entry : resultMap.entrySet()) {
                mergedMap.put(entry.getKey(), mergedMap.getOrDefault(entry.getKey(), 0L) + entry.getValue());
            }
        }

        return mergedMap;
    }

    public static class FileProcessorCallable implements Callable<Map<String, Long>> {
        private final List<String> inputLines;
        private final Map<String, String> predefinedWordsMap;

        public FileProcessorCallable(List<String> inputLines, Map<String, String> predefinedWordsMap) {
            this.inputLines = inputLines;
            this.predefinedWordsMap = predefinedWordsMap;
        }

        @Override
        public Map<String, Long> call() {
            Map<String, Long> matchesMap = new HashMap<>();
            for (String key : predefinedWordsMap.keySet()) {
                matchesMap.put(predefinedWordsMap.get(key), 0L);
            }

            for (String line : inputLines) {
                String[] words = line.split(" ");
                for (int i = 0; i < words.length; i++) {
                    for (String key : predefinedWordsMap.keySet()) {
                        String[] keyWords = key.split(" ");
                        if (doesMatch(words, i, keyWords)) {
                            String originalWord = predefinedWordsMap.get(key);
                            matchesMap.put(originalWord, matchesMap.get(originalWord) + 1);
                        }
                    }
                }
            }

            return matchesMap;
        }

        private boolean doesMatch(String[] words, int idx, String[] phrase) {
            if (phrase.length == 1) {
                return words[idx].equalsIgnoreCase(phrase[0]);
            }

            if (idx + phrase.length > words.length) {
                return false;
            }

            for (int i = 0; i < phrase.length; i++) {
                if (!words[idx + i].equalsIgnoreCase(phrase[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public static List<String> readInputLines(String inputFilePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("IOException Error reading readInputLines: " + e);
        }
        return lines;
    }

    public static void main(String[] args) {
        String inputFile = args[0];
        String predefinedWordsFile = args[1];

        Instant startInstant = Instant.now();

        Map<String, String> predefinedWordsMap = readPredefinedWords(predefinedWordsFile);
        List<String> lines = readInputLines(inputFile);

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int linesPerThread = lines.size() / numberOfThreads;

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<Map<String, Long>>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            int start = i * linesPerThread;
            int end = (i == numberOfThreads - 1) ? lines.size() : start + linesPerThread;
            List<String> subList = lines.subList(start, end);
            FileProcessorCallable task = new FileProcessorCallable(subList, predefinedWordsMap);
            futures.add(executor.submit(task));
        }

        executor.shutdown();

        List<Map<String, Long>> results = new ArrayList<>();
        for (Future<Map<String, Long>> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("InterruptedException Error | ExecutionException Error in main: " + e);
            }
        }

        Map<String, Long> matchesMap = mergeResults(results);

        System.out.println("----------------------------------------------");
        System.out.printf("%-28s %-15s%n", "Predefined Words", "Match Counts");
        System.out.println("----------------------------------------------");
        for (String key : predefinedWordsMap.keySet()) {
            System.out.printf("%-28s %-15d%n", predefinedWordsMap.get(key), matchesMap.get(predefinedWordsMap.get(key)));
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
