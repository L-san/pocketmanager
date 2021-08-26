package ru.lsan.pocketmanager.basepackage.bot.commandhandler;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

@AllArgsConstructor
public class NotificationsHandler extends TimerTask {

    private Owner owner;

    private TelegramBot bot;

    private String name;

    private LocalDateTime date;

    @Override
    public void run() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
        sendMessage.setText(name + " " +date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        bot.send(sendMessage);
    }
}
