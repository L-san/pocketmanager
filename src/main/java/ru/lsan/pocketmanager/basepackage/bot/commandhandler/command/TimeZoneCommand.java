package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;
import ru.lsan.pocketmanager.basepackage.keyboard.TimeZoneKeyboard;

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
        sendMessage.setText("Выберите вашу временную зону.");
        sendMessage.setReplyMarkup(TimeZoneKeyboard.create());
        bot.send(sendMessage);
    }

    public void create(Owner owner, Message message) throws Exception {
        String text = message.getText();
        if(text.startsWith("/")){
            throw new Exception("No Zone");
        }
        ownerService.setZoneAndUpdate(owner,text);
        int chatId = owner.getTelegram_id();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Была установлена зона: "
                +text +"\nТекущее время: "
                + LocalDateTime.now(ZoneId.of(owner.getTimezone())).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        bot.send(sendMessage);
    }


}
