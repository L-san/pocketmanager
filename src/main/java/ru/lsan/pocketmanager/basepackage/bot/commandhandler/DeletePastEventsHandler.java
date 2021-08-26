package ru.lsan.pocketmanager.basepackage.bot.commandhandler;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.command.UpdateTask;
import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@AllArgsConstructor
public class DeletePastEventsHandler extends TimerTask {

    private EventService eventService;

    private Owner owner;

    private final long delay = 24*60*60*1000;

    @Override
    public void run() {
        final String zoneStr = owner.getTimezone();
        List<Event> events = eventService.findAllByOwner(owner);
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(zoneStr));
        for (Event event : events) {
            if (currentTime.compareTo(event.getDate_time())<0){
                eventService.delete(event);
            }
        }

        Timer timer = new Timer();
        timer.schedule(new DeletePastEventsHandler(eventService, owner), delay);
    }
}
