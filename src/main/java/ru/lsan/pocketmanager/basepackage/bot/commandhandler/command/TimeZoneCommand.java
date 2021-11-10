package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class TimeZoneCommand {

    @Autowired
    private TelegramBot bot;

    @Autowired
    private OwnerService ownerService;

    public void handle(Owner owner) {
        int chatId = owner.getTelegram_id();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Введите ваш часовой пояс в виде: GMT+3");
        bot.send(sendMessage);
    }

    public void create(Owner owner, Message message) {
        String text = message.getText();
        int chatId = owner.getTelegram_id();
        try {
            ZoneId.of(text);
            ownerService.setZoneAndUpdate(owner, text);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(text + "\nТекущее время: "
                    + LocalDateTime.now(ZoneId.of(owner.getTimezone())).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            bot.send(sendMessage);
        } catch (DateTimeException e) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText("Что-то не могу найти эту зону в своем списке. Проверьте свои данные и повторите ввод.");
            bot.send(sendMessage);
        }
    }
}
