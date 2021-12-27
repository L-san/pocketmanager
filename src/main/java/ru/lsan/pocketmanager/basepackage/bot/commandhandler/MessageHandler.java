package ru.lsan.pocketmanager.basepackage.bot.commandhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.command.*;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.CalendarService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;
import ru.lsan.pocketmanager.basepackage.keyboard.CalendarKeyboard;

@Component
public class MessageHandler {

    private static final String START_COMMAND = "/start";
    private static final String CREATE_COMMAND = "/create";
    private static final String DATA_COMMAND = "/data";
    private static final String TIME_COMMAND = "/time";
    private static final String EVENT_NAME = "/name";
    private static final String DELETE_COMMAND = "/delete";
    private static final String SCHEDULE_COMMAND = "/schedule";
    private static final String SETTINGS_COMMAND = "/settings";
    private static final String TIMEZONE_COMMAND = "/timezone";
    private static final String CANCEL_COMMAND = "/cancel";

    private static final String CREATE_CALLBACK = "Создать";
    private static final String DELETE_CALLBACK = "Удалить";
    private static final String SCHEDULE_CALLBACK = "Расписание";
    private static final String SETTINGS_CALLBACK = "Настройки";
    private static final String CANCEL_CALLBACK = "Отмена";

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private StartCommand startCommand;

    @Autowired
    private TimeZoneCommand timeZoneCommand;

    @Autowired
    private CreateCallback createCallback;

    @Autowired
    private DeleteCallback deleteCallback;

    @Autowired
    private ScheduleCallback scheduleCallback;

    @Autowired
    private CalendarCallback calendarCallback;

    @Autowired
    private TelegramBot bot;
    //todo порядок навести

    public void handle(Message message, Owner owner) throws InterruptedException {
        String ownerState = owner.getState(); //todo NPE
        if (ownerState == null) ownerState = START_COMMAND;
        String text = message.getText();

        if (text.startsWith(START_COMMAND)) {
            startCommand.handle(message);
            ownerService.setStateAndUpdate(owner, SETTINGS_CALLBACK);
            ownerState = owner.getState();
            text = SETTINGS_CALLBACK;
        }

        if (text.startsWith(CANCEL_CALLBACK)) {
            ownerService.setStateAndUpdate(owner, START_COMMAND);
        }

        if (text.startsWith(CREATE_CALLBACK)) {
            ownerService.setStateAndUpdate(owner, DATA_COMMAND);
            text = DATA_COMMAND;
        }

        if (text.startsWith(DATA_COMMAND)) {
            calendarCallback.handle(owner);
            owner = ownerService.findByTelegramId(owner.getTelegram_id());
            ownerService.setStateAndUpdate(owner, DATA_COMMAND);
        } else if (ownerState.equals(DATA_COMMAND)) {
            if (text.endsWith("Month")) {
                EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
                editMessageReplyMarkup.setMessageId(owner.getMessage_id());
                editMessageReplyMarkup.setChatId(String.valueOf(owner.getTelegram_id()));
                editMessageReplyMarkup.setReplyMarkup(CalendarKeyboard.create(text, owner, calendarService));
                bot.send(editMessageReplyMarkup);
            } else {
                try {
                    calendarCallback.create(text, owner);
                    ownerService.setStateAndUpdate(owner, TIME_COMMAND);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
                    sendMessage.setText("Введите время, например 12:00");
                    bot.send(sendMessage);
                } catch (Exception e) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
                    sendMessage.setText("Что-то не могу ввести данные :с");
                    bot.send(sendMessage);
                }
            }
        }

        if (ownerState.equals(TIME_COMMAND)) {
            calendarCallback.createTime(text, owner);
            ownerService.setStateAndUpdate(owner, CREATE_COMMAND);
            text = CREATE_COMMAND;
        }

        if (ownerState.equals(EVENT_NAME)) {
            try {
                createCallback.create(text, owner);
                ownerService.setStateAndUpdate(owner, START_COMMAND);

            } catch (Exception exception) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Проверьте данные! Возможно, вы собирались запланировать мероприятие в прошлом. У меня нет машины времени!");
                sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
                bot.send(sendMessage);
                ownerService.setStateAndUpdate(owner, START_COMMAND);
            }
        }

        if (text.startsWith(CREATE_COMMAND)) { //todo NPE
            createCallback.handle(owner);
            ownerService.setStateAndUpdate(owner, EVENT_NAME);
        }

        if (text.startsWith(DELETE_CALLBACK)) {
            deleteCallback.handle(owner);
            ownerService.setStateAndUpdate(owner, DELETE_COMMAND);
        } else if (ownerState.equals(DELETE_COMMAND)) {
            try {
                deleteCallback.delete(owner, Integer.parseInt(text)); //todo NumberFormatException
                ownerService.setStateAndUpdate(owner, START_COMMAND);
            } catch (Exception e) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Пам-пам...Ошибочка! Введите только номер(одна цифра) из списка.");
                sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
                bot.send(sendMessage);
            }
        }

        if (text.startsWith(SCHEDULE_CALLBACK)) {
            scheduleCallback.handle(owner);
            ownerService.setStateAndUpdate(owner, START_COMMAND);
        }

        if (text.startsWith(SETTINGS_CALLBACK)) {
            text = TIMEZONE_COMMAND;
        }

        if (text.startsWith(TIMEZONE_COMMAND)) {
            timeZoneCommand.handle(owner);
            ownerService.setStateAndUpdate(owner, TIMEZONE_COMMAND);
        } else if (ownerState.equals(TIMEZONE_COMMAND)) {
            try {
                timeZoneCommand.create(owner, message);
                owner = ownerService.findByTelegramId(owner.getTelegram_id());
                ownerService.setStateAndUpdate(owner, START_COMMAND);
                text = START_COMMAND;
            } catch (Exception ignored) {
                //todo ignored
            }
        }
    }

}
