package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.bot.datautils.DelayUtils;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Timer;

@Component
@EnableAsync
@EnableScheduling
public class TimeSettingsCallback {

    @Autowired
    private TelegramBot bot;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private EventService eventService;

    public void handle(Owner owner) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
        sendMessage.setText("Введите время, в которое вы бы хотели получать ежедневный список дел. Например, 12:00.");
        bot.send(sendMessage);
    }

    public void setTimeSettings(Message message, Owner owner) throws Exception {
        Time time;
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
        try {
            final String zoneStr = owner.getTimezone();
            if(zoneStr==null){
                throw new IllegalArgumentException("Зона не была записана в базу");
            }
            time = Time.valueOf(text + ":00");//todo IllegalArgumentException
            LocalDateTime date = LocalDateTime.of(LocalDate.now(ZoneId.of(zoneStr)), LocalTime.parse(text));
            ownerService.setTimeAndUpdate(owner, time);
            sendMessage.setText("Было установлено время оповещений " + time);

            LocalDateTime prevDateTime = LocalDateTime.now(ZoneId.of(zoneStr));

            long delay = DelayUtils.delayBetween(prevDateTime,date);
            if (delay<0){
                delay *= -1;
            }
            System.out.println("del: "+delay);
            Timer timer = new Timer();
            timer.schedule(new UpdateTask(eventService, bot, owner, time), delay);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        bot.send(sendMessage);
    }

}




