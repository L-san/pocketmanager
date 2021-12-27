package ru.lsan.pocketmanager.basepackage.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.MessageHandler;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.CalendarService;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
@EnableAsync
@EnableScheduling
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private EventService eventService;

    @Autowired
    private CalendarService calendarService;

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

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Owner owner;
        String callback;
        Message message = new Message();
        int userId;

        try {
            userId = Math.toIntExact(update.getMessage().getChatId());
        } catch (Exception e) {
            userId = Math.toIntExact(update.getCallbackQuery().getFrom().getId());
        }
        if (userId != 0) {
            owner = ownerService.findByTelegramId(userId);
        } else {
            throw new UnsupportedOperationException("проблемы с id");
        }
        if (owner == null) {
            Calendar calendar = new GregorianCalendar();

            ownerService.createOwner(Math.toIntExact(userId));
            owner = ownerService.findByTelegramId(Math.toIntExact(userId));
            calendarService.create(owner,calendar.get(Calendar.MONTH));
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

    public Message send(SendMessage message) {
        try {
           return execute(message);
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
        return null;
    }

    public void send(DeleteMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

    public void send(EditMessageReplyMarkup message) {
        try {
            execute(message);
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }
}

