package ru.lsan.pocketmanager.basepackage.bot.datautils;

import java.time.Duration;
import java.time.LocalDateTime;

public class DelayUtils {
    public static long delayBetween(LocalDateTime start, LocalDateTime end){
        Duration duration = Duration.between(start, end);
        return duration.toMillis();
    }
}
