package ru.yandex.practicum.sleeptracker;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp.SessionCountFunction;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp.MaxDuration;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp.MinDuration;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp.AverageTimeSession;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp.SessionBadSleep;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp.CountNoNightSleep;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp.CheckClass;

import java.time.LocalDateTime;
import java.util.*;

public class SleepTrackerAppTest {

    @Test
    public void willReturn3Sessions() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), "normal"));

        SessionCountFunction sessionCountFunction = new SessionCountFunction();

        double result = sessionCountFunction.analyze(sleepingSessions);
        assertEquals(3, result);
        //Должна вернуть кол-во сессий
    }

    @Test
    public void willReturnTheMaximumDelay() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), "normal"));

        MaxDuration maxDuration = new MaxDuration();

        double result = maxDuration.analyze(sleepingSessions);
        assertEquals(8 * 60, result);
        //Должна вернуть максимальную длину  сессии в минутах

        assertNotEquals(7 * 60, result);
        //Проверка что метод не вернул число которое не максимальное

        assertNotEquals(6 * 60, result);
        //Проверка что метод не вернул число которое минимальное
    }


    @Test
    public void willReturnTheMinimumDelay() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), "normal"));

        MinDuration minDuration = new MinDuration();

        double result = minDuration.analyze(sleepingSessions);
        assertEquals(6 * 60, result);
        //Должна вернуть минимальную длину  сессии в минутах

        assertNotEquals(8 * 60, result);
        //Проверка что функция не вернет максимальное число вместо минимальной
    }


    @Test
    public void theAverageNumberTest() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(2), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "normal"));

        AverageTimeSession averageTimeSession = new AverageTimeSession();

        double result = averageTimeSession.analyze(sleepingSessions);

        assertEquals(80, result);
        //Вернет среднее значения сессии
    }

    @Test
    public void theAverageNumberTest0() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "bad"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "normal"));

        AverageTimeSession averageTimeSession = new AverageTimeSession();

        double result = averageTimeSession.analyze(sleepingSessions);

        assertEquals(0, result);
        //Вернет среднее значения сессии
    }

    @Test
    public void willBringBack1BadDream() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "BAD"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "normal"));

        SessionBadSleep sessionBadSleep = new SessionBadSleep();

        double result = sessionBadSleep.analyze(sleepingSessions);

        assertEquals(1, result);
        //Вернет 1 плохой сон(
    }

    @Test
    public void willBringBack0BadDream() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "good"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(0), "normal"));

        SessionBadSleep sessionBadSleep = new SessionBadSleep();

        double result = sessionBadSleep.analyze(sleepingSessions);

        assertEquals(0, result);
        //Вернет 0 плохих снов)
    }

    @Test
    public void itWillReturnThatThereWas1SleeplessNight() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "не бессонная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                7, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 10, 0, 0, 0), "бессонная"));


        CountNoNightSleep countNoNightSleep = new CountNoNightSleep();

        double result = countNoNightSleep.analyze(sleepingSessions);

        assertEquals(1, result);
        //Вернет что была 1 бессоная ночь
    }

    @Test
    public void itWillReturnThatThereWasTwoSleeplessNight() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "не бессонная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                7, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 10, 0, 0, 0), "бессонная"));


        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                17, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 23, 0, 0, 0), "бессонная"));

        CountNoNightSleep countNoNightSleep = new CountNoNightSleep();

        double result = countNoNightSleep.analyze(sleepingSessions);

        assertEquals(2, result);
        //Вернет что была 2 без сонные ночи
    }

    @Test
    public void thereWillBeNoSleepyNights() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "не бессонная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                17, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 23, 0, 0, 0), "бессонная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 31,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 2, 1, 8, 0, 0, 0), "не бессоная"));


        CountNoNightSleep countNoNightSleep = new CountNoNightSleep();

        double result = countNoNightSleep.analyze(sleepingSessions);

        assertEquals(1, result);
        //Вернет что было 2 бессоной ночи не смотря на смена месяца
    }

    @Test
    public void returnThatThereWere2SleeplessNightsDespiteTheDaytimeSleep() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "не бессоная"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                13, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 14, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "дневной сон"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                17, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 23, 0, 0, 0), "бессонная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 31,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 2, 1, 8, 0, 0, 0), "не бессоная"));


        CountNoNightSleep countNoNightSleep = new CountNoNightSleep();

        double result = countNoNightSleep.analyze(sleepingSessions);

        assertEquals(1, result);
        //Вернет что было 2 бессоной ночи не смотря на дневной сон
    }

    @Test
    public void returnThatThereWere0SleeplessNightsDespiteTheDaytimeSleep() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "не бессоная"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                13, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 14, 0, 0, 0), "не бессоная"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 1,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0), "дневной сон"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 31,
                23, 0, 0, 0),
                LocalDateTime.of(2000, 2, 1, 8, 0, 0, 0), "не бессоная"));


        CountNoNightSleep countNoNightSleep = new CountNoNightSleep();

        double result = countNoNightSleep.analyze(sleepingSessions);

        assertEquals(0, result);
        //Вернет что было 0 бессоной ночи не смотря на дневной сон
    }


    @Test
    public void itWillShowThatAPersonIsAnLark() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                19, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 6, 0, 0, 0), "lark"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                1, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 11, 0, 0, 0), "owl"));

        CheckClass checkClass = new CheckClass();

        double result = checkClass.analyze(sleepingSessions);

        assertEquals(1, result);
        //Вернет что хронотип жаворонок, и покажет кол-во раз сколько человек засыпал по этому хронотипу
    }


    @Test
    public void itWillShowThatAPersonIsAnOwl() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                1, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 11, 0, 0, 0), "owl"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                19, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 6, 0, 0, 0), "lark"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                1, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 11, 0, 0, 0), "owl"));

        CheckClass checkClass = new CheckClass();

        double result = checkClass.analyze(sleepingSessions);


        assertEquals(2, result);
        //Вернет что хронотип сова, и покажет кол-во раз сколько челвоек засыпал по этому хронотипу
    }

    @Test
    public void itWillShowThatAPersonIsAnPigeon() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                1, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 11, 0, 0, 0), "owl"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                19, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 6, 0, 0, 0), "lark"));


        CheckClass checkClass = new CheckClass();

        double result = checkClass.analyze(sleepingSessions);


        assertEquals(1, result);
        //Выведет что голубь, и покажет кол-во раз сколько человек засыпал по этому хронотипу
    }

    @Test
    public void ss() {
        List<SleepingSession> sleepingSessions = new ArrayList<>();
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                1, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 11, 0, 0, 0), "owl"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                19, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 6, 0, 0, 0), "lark"));

        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                22, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 10, 0, 0, 0), "pigeon"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                22, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 10, 22, 0, 0), "pigeon"));
        sleepingSessions.add(new SleepingSession(LocalDateTime.of(2000, 1, 2,
                22, 0, 0, 0),
                LocalDateTime.of(2000, 1, 2, 10, 22, 0, 0), "pigeon"));


        CheckClass checkClass = new CheckClass();

        double result = checkClass.analyze(sleepingSessions);


        assertEquals(3, result);
        //Выведет что голубь, и покажет кол-во раз сколько человек засыпал по этому хронотипу
    }


}