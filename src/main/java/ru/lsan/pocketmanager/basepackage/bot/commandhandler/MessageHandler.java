package ru.lsan.pocketmanager.basepackage.bot.commandhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.bot.commandhandler.command.*;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;

@Component
public class MessageHandler {

    private static final String START_COMMAND = "/start";
    private static final String CREATE_CALLBACK = "/create";
    private static final String DELETE_CALLBACK = "/delete";
    private static final String SCHEDULE_CALLBACK = "/schedule";
    private static final String SETTINGS_CALLBACK = "/settings";
    private static final String TIMEZONE_CALLBACK = "/timezone";
    private static final String TIMESET_CALLBACK = "/timeset";
    private static final String CANCEL_CALLBACK = "/cancel";

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private StartCommand startCommand;

    @Autowired
    private TimeZoneCommand timeZoneCommand;

    @Autowired
    private MenuCommand menuCommand;

    @Autowired
    private CreateCallback createCallback;

    @Autowired
    private DeleteCallback deleteCallback;

    @Autowired
    private ScheduleCallback scheduleCallback;

    @Autowired
    private TimeSettingsCallback timeSettingsCallback;

    @Autowired
    private TelegramBot bot;
    //todo порядок навести

    public void handle(Message message, Owner owner) {
        String ownerState = owner.getState(); //todo NPE
        if(ownerState==null) ownerState =  START_COMMAND;
        String text = message.getText();
        if (text.startsWith(START_COMMAND)) {
            startCommand.handle(message);
            ownerService.setStateAndUpdate(owner, START_COMMAND);
            ownerState = owner.getState();
            text = SETTINGS_CALLBACK;
        }

        if (text.startsWith(CANCEL_CALLBACK)) {
            ownerService.setStateAndUpdate(owner, START_COMMAND);
        }

        if(text.startsWith("меню")||text.startsWith("Меню")||text.startsWith("menu")||text.startsWith("/menu")){
            menuCommand.handle(message);
        }

        if (text.startsWith(CREATE_CALLBACK)) {
            createCallback.handle(owner);
            ownerService.setStateAndUpdate(owner, CREATE_CALLBACK);
        } else if (ownerState.equals(CREATE_CALLBACK)) { //todo NPE
            try {
                createCallback.create(text, owner);
                ownerService.setStateAndUpdate(owner, START_COMMAND);
            } catch (Exception exception) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Проверьте корректность введенных данных и попробуйте еще раз");
                sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
                bot.send(sendMessage);
            }
        }

        if (text.startsWith(DELETE_CALLBACK)) {
            deleteCallback.handle(owner);
            ownerService.setStateAndUpdate(owner, DELETE_CALLBACK);
        } else if (ownerState.equals(DELETE_CALLBACK)) {
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

        if(text.startsWith(SETTINGS_CALLBACK)){
            text = TIMEZONE_CALLBACK;
        }

        if (text.startsWith(TIMEZONE_CALLBACK)) {
            timeZoneCommand.handle(owner);
            ownerService.setStateAndUpdate(owner, TIMEZONE_CALLBACK);
        } else if (ownerState.equals(TIMEZONE_CALLBACK)) {
           try{
               timeZoneCommand.create(owner,message);
               owner = ownerService.findByTelegramId(owner.getTelegram_id());
               ownerService.setStateAndUpdate(owner, TIMESET_CALLBACK);
               text = TIMESET_CALLBACK;
           }catch(Exception ignored){
               //todo ignored
           }
        }

        if (text.startsWith(TIMESET_CALLBACK)) {
            timeSettingsCallback.handle(owner);
            ownerService.setStateAndUpdate(owner, TIMESET_CALLBACK);
        } else if (ownerState.equals(TIMESET_CALLBACK)) {
            try {
                timeSettingsCallback.setTimeSettings(message, owner);
                owner = ownerService.findByTelegramId(owner.getTelegram_id());
                ownerService.setStateAndUpdate(owner, START_COMMAND);
            } catch (Exception e) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Что-то я запутался...");
                sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
                bot.send(sendMessage);
            }
        }

    }

}
