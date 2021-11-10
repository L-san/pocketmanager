package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.NotificationsHandler;
import ru.lsan.pocketmanager.basepackage.bot.datautils.DelayUtils;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.CalendarService;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;
import ru.lsan.pocketmanager.basepackage.keyboard.CalendarKeyboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;

@Component
public class CreateCallback {

    @Autowired
    private EventService eventService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private TelegramBot bot;

    public void handle(Owner owner) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id())); //todo NPE??
        sendMessage.setText("Введите название или краткое описание напоминалки");
        bot.send(sendMessage);
    }

    public void create(String text, Owner owner) throws Exception {
        if(text.startsWith("/")){throw new Exception("Command can't be name");}
        int month = owner.getCalendarEntity().getMonth();
        int day = owner.getCalendarEntity().getDay();
        String time = owner.getCalendarEntity().getTime();

        Calendar calendar = new GregorianCalendar();
        if(calendar.get(Calendar.MONTH)>month){
            calendar.roll(Calendar.YEAR,+1);
        }

        String[] hoursMin = time.split(":");
        int hh = Integer.parseInt(hoursMin[0]);
        int mm = Integer.parseInt(hoursMin[1]);

        LocalDateTime date = LocalDateTime.of(
                calendar.get(Calendar.YEAR),
                month+1,
                day,
                hh,
                mm
        );

        Timer timer = new Timer();
        LocalDateTime prevDateTime = LocalDateTime.now(ZoneId.of(owner.getTimezone()));
        long delay = DelayUtils.delayBetween(prevDateTime,date);
        System.out.println(delay);
        timer.schedule(new NotificationsHandler(owner, bot, text, date), delay);

        eventService.createEvent(text,date,owner);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
        sendMessage.setText("Напоминалка создана");
        bot.send(sendMessage);
    }
}
