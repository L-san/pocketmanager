package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.database.entity.CalendarEntity;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.CalendarService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;
import ru.lsan.pocketmanager.basepackage.keyboard.CalendarKeyboard;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class CalendarCallback {

    @Autowired
    private TelegramBot bot;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private OwnerService ownerService;

    public void handle(Owner owner) {
        Calendar calendar = new GregorianCalendar();
        calendarService.setMonthAndUpdate(owner, calendar.get(Calendar.MONTH));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
        sendMessage.setReplyMarkup(CalendarKeyboard.create(null, owner, calendarService));
        sendMessage.setText("Выберите дату:");
        Message message = bot.send(sendMessage);
        ownerService.setMsgIdAndUpdate(owner, message.getMessageId());
    }

    public void create(String text, Owner owner) throws Exception {
        int day = Integer.parseInt(text);
        if (day < 32 && day > 0) {
            calendarService.setDayAndUpdate(owner, day);
        } else {
            throw new Exception("IncorrectData");
        }
    }

    public void createTime(String text, Owner owner){
        calendarService.setTimeAndUpdate(owner, text);
    }
}
