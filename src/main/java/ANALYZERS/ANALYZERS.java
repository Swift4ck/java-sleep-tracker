package ANALYZERS;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ANALYZERS {


    public class TotalSessions implements Function<List<SleepingSession>, SleepAnalysisResult> {
        private static final String DESCRIPTION = "Всего сессий сна: ";

        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            return new SleepAnalysisResult(DESCRIPTION, sessions.size());
        }
    }

    static class MaxDuration implements Function<List<SleepingSession>, SleepAnalysisResult> {
        private static final String DESCRIPTION = "Максимальная сессия сна: ";

        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            long maxDuartion = sessions.stream()
                    .mapToLong(session -> ChronoUnit.MINUTES.between(session.getStart(),
                            session.getEnd()))
                    .max()
                    .orElse(0);
            return new SleepAnalysisResult(DESCRIPTION, maxDuartion);
        }
    }

    static class MinDuration implements Function<List<SleepingSession>, SleepAnalysisResult> {
        private static final String DESCRIPTION = "Минимальная сессия сна: ";

        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            long minDuartion = sessions.stream()
                    .mapToLong(session -> ChronoUnit.MINUTES.between(session.getStart(),
                            session.getEnd()))
                    .min()
                    .orElse(0);
            return new SleepAnalysisResult(DESCRIPTION, minDuartion);
        }
    }


    static class AverageTimeSession implements Function<List<SleepingSession>, SleepAnalysisResult> {
        private static final String DESCRIPTION = "Среднее сессия сна: ";

        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            double maxDuartion = sessions.stream()
                    .mapToLong(session -> ChronoUnit.MINUTES.between(session.getStart(),
                            session.getEnd()))
                    .average()
                    .orElse(0);

            return new SleepAnalysisResult(DESCRIPTION, maxDuartion);
        }
    }


    static class SessionBadSleep implements Function<List<SleepingSession>, SleepAnalysisResult> {
        private static final String DESCRIPTION = "Количество плохих сессий сна: ";

        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            long badSession = sessions.stream()
                    .filter(session -> session.getStatus().equals("BAD"))
                    .count();

            return new SleepAnalysisResult(DESCRIPTION, badSession);
        }
    }

    public class SleeplessNights implements Function<List<SleepingSession>, SleepAnalysisResult> {


        private static final String DESCRIPTION = "Бессонных ночей: ";

        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            if (sessions.isEmpty()) {
                return new SleepAnalysisResult(DESCRIPTION, 0);
            }

            LocalDateTime firstStart = sessions.get(0).getStart();
            LocalDateTime lastEnd = sessions.get(sessions.size() - 1).getEnd();

            LocalDate firstNight = firstStart.getHour() >= 12
                    ? firstStart.toLocalDate().plusDays(1)
                    : firstStart.toLocalDate();

            LocalDate lastNight = lastEnd.toLocalDate();

            long totalNights = ChronoUnit.DAYS.between(firstNight, lastNight.plusDays(1));

            Set<LocalDate> nightsWithSleep = sessions.stream()
                    .filter(SleepingSession::isNightSession)
                    .map(this::getNightDate)
                    .collect(Collectors.toSet());

            long sleeplessNights = totalNights - nightsWithSleep.size();
            return new SleepAnalysisResult(DESCRIPTION, Math.max(0, sleeplessNights));
        }

        private LocalDate getNightDate(SleepingSession session) {
            LocalDateTime start = session.getStart();
            return start.getHour() < 12
                    ? start.toLocalDate().minusDays(1)
                    : start.toLocalDate();
        }
    }

    public class CheckClass implements Function<List<SleepingSession>, SleepAnalysisResult> {
        String DESCRIPTION = "Бессонных ночей";


        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            long owlCount = sessions.stream()
                    .filter(session -> {
                        LocalDateTime start = session.getStart();
                        LocalDateTime end = session.getEnd();

                        LocalDateTime nightStart = start.toLocalDate().atStartOfDay();
                        LocalDateTime endNight = nightStart.plusHours(6);


                        return (start.isAfter(nightStart) && end.isAfter(endNight))||
                                (start.isAfter(nightStart) && end.isBefore(endNight));

                    })
                    .filter(session -> {
                        LocalDateTime start = session.getStart();
                        LocalDateTime end = session.getEnd();

                        LocalDateTime startOwl;
                        LocalDateTime endOwl = end.toLocalDate().atTime(9, 0);
                        //И здесь вроде была ошибка, он проверял тот же день и получалось что допустим час ночи находился позже 22 часов вечера
                        //Вообще как лучше делать такие проверки, что бы учитывались дни?
                        if (end.getDayOfMonth() > start.getDayOfMonth()){
                            startOwl = start.toLocalDate().atTime(23, 0).minusDays(1);
                        }
                        else {
                            startOwl = start.toLocalDate().atTime(23, 0);
                        }

                        return (start.isAfter(startOwl) || start.isAfter(startOwl.plusHours(1))) && end.isAfter(endOwl);
                    })
                    .count();

            long larkCount = sessions.stream()
                    .filter(session -> {
                        LocalTime start = session.getStart().toLocalTime();
                        LocalTime end = session.getEnd().toLocalTime();

                        LocalTime dayStartSleep = LocalTime.of(12, 0);
                        LocalTime dayEndSleep = LocalTime.of(18, 0);

                        return !(//ошибка была тут, я забыл поставить "!" из-за этого он наоборот пропускал только дневной сон
                                (dayStartSleep.isAfter(start) && dayEndSleep.isBefore(end)) ||
                                        (dayStartSleep.isAfter(start) && dayEndSleep.isAfter(end)) ||
                                        (dayStartSleep.isBefore(start) && dayEndSleep.isBefore(end))
                        );
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
                        LocalDateTime endLark = session.getEnd().withHour(7);

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
                DESCRIPTION = "Ваш хронотип сова, вы засыпали позже 23;00, а пробуждение после 9:00, столько раз: ";
                return new SleepAnalysisResult(DESCRIPTION, owlCount);
            } else if (larkCount > owlCount && larkCount > pigeonCount) {
                DESCRIPTION = "Ваш хронотип жаворонок, вы ложились, раньше 22:00 " +
                        "и просыпались до 7:00, вот сколько раз: ";
                return new SleepAnalysisResult(DESCRIPTION, larkCount);
            } else if (pigeonCount > larkCount && pigeonCount > owlCount) {
                DESCRIPTION = "Ваш хронотип голубь, вы ложились  спать в другое время в " +
                        "отличие от сов и жаворонков, такое кол-во раз: ";
                return new SleepAnalysisResult(DESCRIPTION, pigeonCount);
            } else {
                DESCRIPTION = "Ваш хронотип голубь, вы ложились одинаково как сова и жаворонок, такое кол-во раз: ";
                return new SleepAnalysisResult(DESCRIPTION, pigeonCount + owlCount);
            }
        }

    }


}