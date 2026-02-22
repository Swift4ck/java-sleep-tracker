package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

public class SleepingSession {
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;

    public SleepingSession(LocalDateTime start, LocalDateTime end, String status) {
        this.start = start;
        this.end = end;
        this.status = status;
    }

//убрал от сюда toString вывод аналитки теперь в отдельном классе

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getStatus() {
        return status;
    }


    public boolean isNightSession() {

        //Распишу что бы показать что понял как работает метод
        LocalDateTime start = getStart(); //выставляем начало сесси сна
        LocalDateTime end = getEnd(); //выставляем конец сесси сна


        LocalDateTime nightStart = start.toLocalDate().atStartOfDay(); //Старт ночи с 00:00 и тут используется  метод
        //вместо ручного выставления как я делал

        while (!nightStart.isAfter(end)) {
            LocalDateTime nightEnd = nightStart.plusHours(6); //выставляем конец ночи

            // проверяем пересечение, ставим отрицание , и проверяем что начало ночи находится до начала старта ночи
            //начало сна находится после начало ночи
            boolean overlaps = !end.isBefore(nightStart) && !start.isAfter(nightEnd);
            if (overlaps) {
                return true;
            }
            nightStart = nightStart.plusDays(1);
        }
        return false;// посути этот метод првоеряет явялется ли сессия сна ночной, если да то возрашает ложь, если сессия ночная то истина
    } /


}