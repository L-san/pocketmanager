package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;

import java.util.List;

@Component
public class DeleteCallback {

    @Autowired
    private TelegramBot bot;

    @Autowired
    private EventService eventService;

    @Autowired
    private OwnerService ownerService;

    public void handle(Owner owner) {
        List<Event> events = eventService.findAllByOwner(owner);
        StringBuilder str = new StringBuilder();
        str.append("Текущий список дел:");
        for (Event e : events) {
            str.append("\n").append(e.getId()).append(" ").append(e.getName()).append(" ").append(e.getParsedDate());
        }
        str.append("\nВыберите номер события, которое следует удалить и пришлите его в новом сообщении.");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
        sendMessage.setText(str.toString());
        bot.send(sendMessage);
    }

    public void delete(Owner owner, int id) {
        eventService.delete(eventService.getEventById(id));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(owner.getTelegram_id()));
        sendMessage.setText("Событие удалено.");
        bot.send(sendMessage);
    }
}
