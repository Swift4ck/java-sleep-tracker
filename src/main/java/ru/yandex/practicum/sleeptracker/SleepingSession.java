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

    @Override
    public String toString() {
        return "SleepingSession [startTime=" + start + ", endTime=" + end + ", status=" + status + "]";
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getStatus() {
        return status;
    }


}
