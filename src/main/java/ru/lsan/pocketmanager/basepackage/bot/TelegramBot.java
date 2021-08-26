package ru.lsan.pocketmanager.basepackage.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.DeletePastEventsHandler;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.MessageHandler;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

@Component
@EnableAsync
@EnableScheduling
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private EventService eventService;

    @Autowired
    private MessageHandler messageHandler;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Owner owner;
        String callback;
        Message message = new Message();
        int userId;

        try {
            userId = Math.toIntExact(update.getMessage().getChatId());
        } catch (Exception e) {
            userId = update.getCallbackQuery().getFrom().getId();
        }
        if (userId != 0) {
            owner = ownerService.findByTelegramId(userId);
        } else {
            throw new UnsupportedOperationException("проблемы с id");
        }
        if (owner == null) {
            ownerService.createOwner(Math.toIntExact(userId));
            owner = ownerService.findByTelegramId(Math.toIntExact(userId));

            Timer timer = new Timer();

            //todo
            String prevDateTime = new SimpleDateFormat("dd MM yyyy HH mm ss").format(new Date(System.currentTimeMillis()));
            String[] prevTimes = prevDateTime.split(" ");

            int prevHour = Integer.parseInt(prevTimes[3]);
            int prevMinute = Integer.parseInt(prevTimes[4]);
            int prevSecond = Integer.parseInt(prevTimes[5]);

            long delay = 1000*(24 * 60 * 60 - (prevSecond + prevMinute * 60 + prevHour * 60 * 60));
            System.out.println("delay "+delay);
            System.out.println("time in zone "+prevDateTime);
            timer.schedule(new DeletePastEventsHandler(eventService, owner), delay);
        }


        if (update.hasMessage()) {
            message = update.getMessage();
            messageHandler.handle(message, owner);
        } else {
            callback = update.getCallbackQuery().getData();
            if (!callback.isEmpty()) {
                message.setText(callback);
                messageHandler.handle(message, owner);
            }
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

    public void send(DeleteMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

}
