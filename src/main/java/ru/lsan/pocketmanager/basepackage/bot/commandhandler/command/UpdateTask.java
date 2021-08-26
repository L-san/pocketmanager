package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.bot.datautils.DelayUtils;
import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@EnableAsync
@EnableScheduling
@AllArgsConstructor
public class UpdateTask extends TimerTask {

    private final EventService eventService;

    private final TelegramBot bot;

    private final Owner owner;

    private final Time time;

    @Override
    public void run() {
        try {
            List<Event> events = eventService.findAllByOwner(owner);
            StringBuilder str = new StringBuilder();
            str.append("Текущий список дел:");
            TreeMap<LocalDateTime, String> eventMap = new TreeMap<>();
            for (Event event : events) {
                eventMap.put(event.getDate_time(), event.getName());
            }
             NavigableMap<LocalDateTime,String> map  = eventMap.descendingMap();
            LocalDateTime[] dates = eventMap.keySet().toArray(new LocalDateTime[]{});
            for(LocalDateTime s: dates){
                str.append("\n").append(map.get(s)).append(" ").append(s.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            }

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
            sendMessage.setText(str.toString());
            bot.send(sendMessage);


            LocalDateTime prevDateTime = LocalDateTime.now(ZoneId.of(owner.getTimezone()));
            LocalDateTime nextTime = LocalDateTime.now(ZoneId.of(owner.getTimezone()));
            nextTime = nextTime.plusDays(1);

            long delay = DelayUtils.delayBetween(prevDateTime,nextTime);
            if (delay<0){
                delay *= -1;
            }

            Timer timer = new Timer();
            timer.schedule(new UpdateTask(eventService, bot, owner, time), delay);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Parse err");
        }

    }

}
