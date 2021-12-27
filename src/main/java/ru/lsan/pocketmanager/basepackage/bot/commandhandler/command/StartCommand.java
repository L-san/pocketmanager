package ru.lsan.pocketmanager.basepackage.bot.commandhandler.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.keyboard.MenuKeyboard;

@Component
public class StartCommand {

    @Autowired
    private TelegramBot bot;

    public void handle(Message message) {

        Long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Приветствую! Я Pocket Manager Bot - ваш личный помощник в планировании дел.\n" +
                            "В мои обязанности входит:\n" +
                            "> ведение списка дел;\n" +
                            "> оповещения и напоминалки.\n" +
                            "Добавляйте и удаляйте заметки при помощи кнопок \"Запланировать\" и \"Удалить\".\n" +
                            "Список текущих дел вы можете посмотреть в разделе \"Расписание\".\n" +
                            "Часовой пояс вы всегда можете изменить в разделе \"Настройки\".\n");
        sendMessage.setReplyMarkup(MenuKeyboard.create());
        bot.send(sendMessage);
    }

}
