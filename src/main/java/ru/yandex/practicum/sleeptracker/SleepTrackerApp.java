package ru.yandex.practicum.sleeptracker;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SleepTrackerApp {
    //Работа не сделана  до конца у меня не получается сделать последнюю  функцию, пожалуйста посмотрите и прокомментируете  что в ней не так
    private List<SleepAnalyticsFunction> functions;

    public SleepTrackerApp() {
        functions = new ArrayList<>();
        functions.add(new SessionCountFunction());
        functions.add(new MaxDuration());
        functions.add(new MinDuration());
        functions.add(new AverageTimeSession());
        functions.add(new SessionBadSleep());
        functions.add(new CountNoNightSleep());
        functions.add(new CheckClass());

    }

    public static void main(String[] args) throws IOException {
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

            app.functions.forEach(function -> {
                if (function instanceof SessionCountFunction) {
                    System.out.println("Количество сессий сна: " + function.analyze(sleepingSessions));
                } else if (function instanceof MaxDuration) {
                    System.out.println("Максимальная продолжительность сессии (в минутах): " +
                            function.analyze(sleepingSessions));
                } else if (function instanceof MinDuration) {
                    System.out.println("Минимальная продолжительность ссесии (в минутах): " +
                            function.analyze(sleepingSessions));
                } else if (function instanceof AverageTimeSession) {
                    System.out.println("Средняя продолжительность сессии (в минутах); " +
                            function.analyze(sleepingSessions));
                } else if (function instanceof SessionBadSleep) {
                    System.out.println("Количество сессий с плохим качество сна: " +
                            function.analyze(sleepingSessions));
                } else if (function instanceof CountNoNightSleep) {
                    System.out.println("Количество без сонных ночей: " + function.analyze(sleepingSessions));
                } else if (function instanceof CheckClass) {
                    System.out.println(function.analyze(sleepingSessions));
                }

            });


        }


    }

    @FunctionalInterface
    interface SleepAnalyticsFunction {
        double analyze(List<SleepingSession> sessions);
    }

    static class SessionCountFunction implements SleepAnalyticsFunction {
        @Override
        public double analyze(List<SleepingSession> sessions) {
            return (double) sessions.size();
        }
    }

    static class MaxDuration implements SleepAnalyticsFunction {
        @Override
        public double analyze(List<SleepingSession> sessions) {
            return sessions.stream()
                    .mapToLong(session -> ChronoUnit.MINUTES.between(session.getStart(),
                            session.getEnd()))
                    .max()
                    .orElse(0);

        }
    }

    static class MinDuration implements SleepAnalyticsFunction {
        @Override
        public double analyze(List<SleepingSession> sessions) {
            return sessions.stream()
                    .mapToLong(session -> ChronoUnit.MINUTES.between(session.getStart(),
                            session.getEnd()))
                    .min()
                    .orElse(0);

        }
    }

    static class AverageTimeSession implements SleepAnalyticsFunction {
        @Override
        public double analyze(List<SleepingSession> sessions) {
            return sessions.stream()
                    .mapToLong(session -> ChronoUnit.MINUTES.between(session.getStart(),
                            session.getEnd()))
                    .average()
                    .orElse(0.0);
        }
    }

    static class SessionBadSleep implements SleepAnalyticsFunction {
        @Override
        public double analyze(List<SleepingSession> sessions) {
            return sessions.stream()
                    .filter(sleepingSession -> sleepingSession.getStatus().equals("BAD"))
                    .count();
        }
    }


    static class CountNoNightSleep implements SleepAnalyticsFunction {
        @Override
        public double analyze(List<SleepingSession> sessions) {

            return sessions.stream()
                    .filter(session -> {
                        LocalDateTime start = session.getStart();
                        LocalDateTime end = session.getEnd();

                        LocalDateTime nextDay = session.getStart().toLocalDate().plusDays(1).atTime(0, 0);


                        return !(end.isAfter(nextDay));
                    })
                    .count();
        }
    }

    //Эта функция не работает правильно, и я пока что не могу понять почему, пожалуйста дайте комментарии  в чем я ошибся тут
    static class CheckClass implements SleepAnalyticsFunction {
        @Override
        public double analyze(List<SleepingSession> sessions) {

            long owlCount = sessions.stream()
                    .filter(session -> {
                        LocalTime start = session.getStart().toLocalTime();
                        LocalTime end = session.getEnd().toLocalTime();

                        LocalTime dayStartSleep = LocalTime.of(12, 0);
                        LocalTime dayEndSleep = LocalTime.of(18, 0);

                        return (dayStartSleep.isAfter(start) && dayEndSleep.isBefore(end)) ||
                                (dayStartSleep.isAfter(start) && dayEndSleep.isAfter(end)) ||
                                (dayStartSleep.isBefore(start) && dayEndSleep.isBefore(end));
                    })
                    .filter(session -> {
                        LocalDateTime start = session.getStart();
                        LocalDateTime end = session.getEnd();

                        LocalDateTime nextDay = session.getStart().toLocalDate().plusDays(1).atTime(0, 0);


                        return (end.isAfter(nextDay));
                    })
                    .filter(session -> {
                        LocalDateTime start = session.getStart();
                        LocalDateTime end = session.getEnd();

                        LocalDateTime startOwl = session.getStart().withHour(22);
                        LocalDateTime endOwl = session.getEnd().withHour(6);


                        return start.isAfter(startOwl) && end.isAfter(endOwl);
                    })
                    .count();


            long larkCount = sessions.stream()
                    .filter(session -> {
                        LocalTime start = session.getStart().toLocalTime();
                        LocalTime end = session.getEnd().toLocalTime();

                        LocalTime dayStartSleep = LocalTime.of(12, 0);
                        LocalTime dayEndSleep = LocalTime.of(18, 0);

                        return (dayStartSleep.isAfter(start) && dayEndSleep.isBefore(end)) ||
                                (dayStartSleep.isAfter(start) && dayEndSleep.isAfter(end)) ||
                                (dayStartSleep.isBefore(start) && dayEndSleep.isBefore(end));
                    })
                    .filter(session -> {
                        LocalDateTime start = session.getStart();
                        LocalDateTime end = session.getEnd();

                        LocalDateTime nextDay = session.getStart().toLocalDate().plusDays(1).atTime(0, 0);


                        return (end.isAfter(nextDay));
                    })
                    .filter(session -> {
                        LocalDateTime start = session.getStart();
                        LocalDateTime end = session.getEnd();

                        LocalDateTime startLark = session.getStart().withHour(22);
                        LocalDateTime endLark = session.getEnd().withHour(6);

                        return start.isBefore(startLark) && end.isBefore(endLark);
                    })
                    .count();

            long pigeonCount = sessions.stream()
                    .filter(session -> {
                        LocalTime start = session.getStart().toLocalTime();
                        LocalTime end = session.getEnd().toLocalTime();

                        LocalTime dayStartSleep = LocalTime.of(12, 0);
                        LocalTime dayEndSleep = LocalTime.of(18, 0);

                        return (dayStartSleep.isAfter(start) && dayEndSleep.isBefore(end)) ||
                                (dayStartSleep.isAfter(start) && dayEndSleep.isAfter(end)) ||
                                (dayStartSleep.isBefore(start) && dayEndSleep.isBefore(end));
                    })
                    .filter(session -> {
                        LocalDateTime start = session.getStart();
                        LocalDateTime end = session.getEnd();

                        LocalDateTime nextDay = session.getStart().toLocalDate().plusDays(1).atTime(0, 0);


                        return (end.isAfter(nextDay));
                    })
                    .count() + sessions.size() - (owlCount + larkCount);


            if (owlCount > larkCount && owlCount > pigeonCount) {
                System.out.print("Ваш хронотип сова, вы засыпали позже 23;00, а пробуждение после 9:00, столько раз: ");
                return owlCount;
            } else if (larkCount > owlCount && larkCount > pigeonCount) {
                System.out.print("Ваш хронотип жаворонок, вы ложились, раньше 22:00 " +
                        "и просыпались до 7:00, вот сколько раз ");
                return larkCount;
            } else if (pigeonCount > larkCount && pigeonCount > owlCount) {
                System.out.print("Ваш хронотип голубь, вы ложились  спать в другое время в " +
                        "отличие от сов и жаворонков, такое кол-во раз: ");
                return pigeonCount;
            } else {
                System.out.print("Ваш хронотип голубь, вы ложились одинаково как сова и жаворонок, такое кол-во раз: ");
                return pigeonCount + owlCount;
            }
        }
    }


}