package ru.lsan.pocketmanager.basepackage.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class MenuKeyboard {

    public static InlineKeyboardMarkup create(){
        return KeyboardUtils.inlineOf(KeyboardUtils.buttonOf("Запланировать", "/create"),
                                      KeyboardUtils.buttonOf("Удалить", "/delete"),
                                      KeyboardUtils.buttonOf("Расписание", "/schedule"),
                                      KeyboardUtils.buttonOf("Настройки", "/settings"),
                                      KeyboardUtils.buttonOf("Отмена", "/cancel"));
    }
}
