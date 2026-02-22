package ru.yandex.practicum.sleeptracker;

import analyzers.Analyzers.TotalSessions;
import analyzers.Analyzers.MaxDuration;
import analyzers.Analyzers.MinDuration;
import analyzers.Analyzers.AverageTimeSession;
import analyzers.Analyzers.SessionBadSleep;
import analyzers.Analyzers.SleeplessNights;
import analyzers.Analyzers.CheckClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SleepTrackerApp {
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> Analyzers = Arrays.asList(
            new TotalSessions(),
            new MaxDuration(),
            new MinDuration(),
            new AverageTimeSession(),
            new SessionBadSleep(),
            new SleeplessNights(),
            new CheckClass()
    );


    public static void main(String[] args) { // Переделал main как вы сказали
        SleepTrackerApp app = new SleepTrackerApp();

        System.out.println("Добро пожаловать в аналитику вашего сна");

        try (Stream<String> readSleep = Files.lines(Paths.get("src/main/resources/sleep_log.txt"))) {
            List<SleepingSession> sleepingSessions = readSleep
                    .map(line -> line.split(";"))
                    .map(parts -> {
                        LocalDateTime start = LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
                        LocalDateTime end = LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
                        return new SleepingSession(start, end, parts[2]);
                    })
                    .collect(Collectors.toList());

            Analyzers.stream()
                    .map(analyzer -> analyzer.apply(sleepingSessions))
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Файл не найден: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }


}