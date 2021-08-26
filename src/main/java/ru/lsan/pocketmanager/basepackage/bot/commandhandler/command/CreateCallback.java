package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.DeletePastEventsHandler;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.NotificationsHandler;
import ru.lsan.pocketmanager.basepackage.bot.datautils.DelayUtils;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        sendMessage.setText("Введите название или краткое описание напоминалки, а также время и дату. Название от времени и даты следует отделить запятой.\n" +
                            "Например:\"Покормить кота, 12.09.2021 12:00\".");
       // sendMessage.setReplyMarkup(MenuKeyboard.create());
        bot.send(sendMessage);
    }

    public void create(String text, Owner owner) throws Exception {
        LocalDateTime date;
        String[] nameTime = text.split(", ");
        String name = nameTime[0];
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            //date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(nameTime[1]);
            date = LocalDateTime.parse(nameTime[1], formatter);
        }catch(Exception e){
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm");
                date = LocalDateTime.parse(nameTime[1], formatter);
                //date = new SimpleDateFormat("dd.MM.yyyy HH.mm").parse(nameTime[1]);
            }catch(Exception ex){
                throw new Exception("Error parsing Date");
            }
        }
        eventService.createEvent(name,date,owner);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
        sendMessage.setText("Напоминалка создана");
        bot.send(sendMessage);

        Timer timer = new Timer();
        LocalDateTime prevDateTime = LocalDateTime.now(ZoneId.of(owner.getTimezone()));
        long delay = DelayUtils.delayBetween(prevDateTime,date);
        timer.schedule(new NotificationsHandler(owner, bot, name, date), delay);
    }
}
