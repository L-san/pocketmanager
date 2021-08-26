package ru.lsan.pocketmanager.basepackage.bot.commandhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;

@Component
public class OwnerHandler{

    @Autowired
    private OwnerService ownerService;

    public void handle(Message message) {
        Long chatId = message.getChatId();
        if(chatId!=null){
            if(ownerService.findByTelegramId(Math.toIntExact(chatId))==null){
                ownerService.createOwner(Math.toIntExact(chatId));
                System.out.println(ownerService.findByTelegramId(Math.toIntExact(chatId)).getId());
            }
        }
    }
}
