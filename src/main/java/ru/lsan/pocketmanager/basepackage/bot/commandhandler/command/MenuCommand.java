package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.keyboard.MenuKeyboard;

@Component
public class MenuCommand {

    @Autowired
    private TelegramBot bot;

    public void handle(Message message) {

        Long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Добавляйте и удаляйте заметки при помощи кнопок \"Запланировать\" и \"Удалить\".\n" +
                "Список текущих дел вы можете посмотреть в разделе \"Расписание\". Записи за предыдущий день удаляются ежедневно в 00:00.\n" +
                "Каждый день я буду присылать вам список дел на следующие 24 часа. Время оповещений вы всегда можете изменить в разделе \"Настройки\".\n" +
                "Для того, чтобы воспользоваться меню, просто напишите в чат \"Меню\".");
        sendMessage.setReplyMarkup(MenuKeyboard.create());
        bot.send(sendMessage);
    }

}
