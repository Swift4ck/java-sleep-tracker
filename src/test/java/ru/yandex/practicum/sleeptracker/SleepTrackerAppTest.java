package ru.yandex.practicum.sleeptracker;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ANALYZERS.ANALYZERS;
import org.junit.jupiter.api.Test;
import ANALYZERS.ANALYZERS.TotalSessions;
import ANALYZERS.ANALYZERS.MaxDuration;
import ANALYZERS.ANALYZERS.MinDuration;
import ANALYZERS.ANALYZERS.AverageTimeSession;
import ANALYZERS.ANALYZERS.SessionBadSleep;
import ANALYZERS.ANALYZERS.SleeplessNights;
import ANALYZERS.ANALYZERS.CheckClass;

import java.time.LocalDateTime;
import java.util.*;

public class SleepTrackerAppTest {

    @Test
    public void willReturn3Sessions() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), "normal"));

        TotalSessions sessionCountFunction = new TotalSessions();

        SleepAnalysisResult result = sessionCountFunction.apply(sleepingSessions);
        assertEquals(3, result.getValue());
        //Должна вернуть кол-во сессий
    }

    @Test
    public void willReturnTheMaximumDelay() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), "normal"));

        MaxDuration maxDuration = new MaxDuration();

        SleepAnalysisResult result = maxDuration.apply(sleepingSessions);
        assertEquals(8 * 60, (long) result.getValue());
        //Должна вернуть максимальную длину  сессии в минутах

        assertNotEquals(7 * 60, (long) result.getValue());
        //Проверка что метод не вернул число которое не максимальное

        assertNotEquals(6 * 60, (long) result.getValue());
        //Проверка что метод не вернул число которое минимальное
    }


    @Test
    public void willReturnTheMinimumDelay() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), "normal"));

        MinDuration minDuration = new MinDuration();

        SleepAnalysisResult result = minDuration.apply(sleepingSessions);
        assertEquals(6 * 60, (long) result.getValue());
        //Должна вернуть минимальную длину  сессии в минутах

        assertNotEquals(8 * 60, (long) result.getValue());
        //Проверка что функция не вернет максимальное число вместо минимальной
    }


    @Test
    public void theAverageNumberTest() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(2), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "normal"));

        AverageTimeSession averageTimeSession = new AverageTimeSession();

        SleepAnalysisResult result = averageTimeSession.apply(sleepingSessions);

        assertEquals(80.0, (double) result.getValue());
        //Вернет среднее значения сессии

    }

    @Test
    public void theAverageNumberTest0() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "normal"));

        AverageTimeSession averageTimeSession = new AverageTimeSession();

        SleepAnalysisResult result = averageTimeSession.apply(sleepingSessions);

        assertEquals(0.0, result.getValue());
        //Вернет среднее значения сессии
    }

    @Test
    public void willBringBack1BadDream() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "BAD"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "normal"));

        SessionBadSleep sessionBadSleep = new SessionBadSleep();

        SleepAnalysisResult result = sessionBadSleep.apply(sleepingSessions);

        assertEquals(1, (long) result.getValue());
        //Вернет 1 плохой сон(
    }

    @Test
    public void willBringBack0BadDream() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "normal"));

        SessionBadSleep sessionBadSleep = new SessionBadSleep();

        SleepAnalysisResult result = sessionBadSleep.apply(sleepingSessions);

        assertEquals(0, (long) result.getValue());
        //Вернет 0 плохих снов)
    }

    @Test
    public void itWillReturnThatThereWas1SleeplessNight() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 8, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 3, 8, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 4,
                7, 0, 0, 0),
                LocalDateTime.of(2000, 1, 4, 10, 0, 0, 0), "бессоная"));


        SleeplessNights countNoNightSleep = new SleeplessNights();

        SleepAnalysisResult result = countNoNightSleep.apply(sleepingSessions);

        assertEquals(1, (long) result.getValue());
        //Вернет что была 1 бессоная ночь
    }

    @Test
    public void itWillReturnThatThereWasTwoSleeplessNight() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 2, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 2, 2, 8, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 2, 2,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 2, 3, 8, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 2, 4,
                7, 0, 0, 0),
                LocalDateTime.of(2000, 2, 4, 10, 0, 0, 0), "бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 2, 4,
                18, 0, 0, 0),
                LocalDateTime.of(2000, 2, 5, 8, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 2, 6,
                7, 0, 0, 0),
                LocalDateTime.of(2000, 2, 6, 10, 0, 0, 0), "бессоная"));

        SleeplessNights countNoNightSleep = new SleeplessNights();

        SleepAnalysisResult result = countNoNightSleep.apply(sleepingSessions);

        assertEquals(2, (long) result.getValue());
        //Вернет что была 2 без сонные ночи
    }


    @Test
    public void itWillShowThatAPersonIsAnLark() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2025, 2, 2,
                19, 0, 0, 0),
                LocalDateTime.of(2025, 2, 3, 5, 0, 0, 0), "lark"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2025, 2, 3,
                19, 0, 0, 0),
                LocalDateTime.of(2025, 2, 4, 5, 0, 0, 0), "lark"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2025, 2, 5,
                1, 0, 0, 0),
                LocalDateTime.of(2025, 2, 5, 11, 0, 0, 0), "owl"));


        CheckClass test = new CheckClass();

        SleepAnalysisResult result = test.apply(sleepingSessions);

        assertEquals(2, (long) result.getValue());

        assertEquals("Ваш хронотип жаворонок, вы ложились, раньше 22:00 и просыпались до 7:00, вот сколько раз: ",
                result.getDescription());

        //Вернет что хронотип жаворонок, и покажет кол-во раз сколько человек засыпал по этому хронотипу
    }


    @Test
    public void itWillShowThatAPersonIsAnOwl() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2025, 2, 3,
                23, 30, 0, 0),
                LocalDateTime.of(2025, 2, 4, 11, 0, 0, 0), "owl"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2025, 2, 4,
                23, 30, 0, 0),
                LocalDateTime.of(2025, 2, 5, 11, 0, 0, 0), "owl"));

        CheckClass test = new CheckClass();

        SleepAnalysisResult result = test.apply(sleepingSessions);


        assertEquals(2, (long) result.getValue());

        assertEquals("Ваш хронотип сова, вы засыпали позже 23;00, а пробуждение после 9:00, столько раз: ",
                result.getDescription());

        //Вернет что хронотип сова, и покажет кол-во раз сколько человек засыпал по этому хронотипу
    }

    @Test
    public void itWillShowThatAPersonIsAnPigeon() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                19, 0, 0, 0),
                LocalDateTime.of(2000, 1, 3, 6, 0, 0, 0), "lark"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                1, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 11, 0, 0, 0), "owl"));


        CheckClass checkClass = new CheckClass();

        SleepAnalysisResult result = checkClass.apply(sleepingSessions);


        assertEquals(1, (long) result.getValue());

        assertEquals("Ваш хронотип голубь, вы ложились одинаково как сова и жаворонок, такое кол-во раз: ",
                result.getDescription());


        //Выведет что голубь, потому что равное кол-во хронотипов, и покажет кол-во раз сколько человек засыпал по этому хронотипу
    }

    @Test
    public void aPigeonIsSelected() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                22, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 8, 0, 0, 0), "pigeon"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                22, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 8, 0, 0, 0), "pigeon"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                19, 0, 0, 0),
                LocalDateTime.of(2000, 1, 3, 6, 0, 0, 0), "lark"));


        CheckClass checkClass = new CheckClass();

        SleepAnalysisResult result = checkClass.apply(sleepingSessions);

        assertEquals(2, (long) result.getValue());

        assertEquals("Ваш хронотип голубь, вы ложились  спать в другое время в отличие от сов и жаворонков, " +
                        "такое кол-во раз: ",
                result.getDescription());
        //Выведет что голубь, и покажет кол-во раз сколько человек засыпал по этому хронотипу
    }


}