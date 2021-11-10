package ru.lsan.pocketmanager.basepackage.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MenuKeyboard {

    public static ReplyKeyboardMarkup create(){
        List<KeyboardRow> keyboard = new ArrayList<>(5);
        KeyboardRow k1 = new KeyboardRow();
        k1.add(new KeyboardButton("Создать"));
        k1.add(new KeyboardButton("Удалить"));
        k1.add(new KeyboardButton("Расписание"));
        KeyboardRow k2 = new KeyboardRow();
        k2.add(new KeyboardButton("Настройки"));
        k2.add(new KeyboardButton("Отмена"));
        keyboard.add(k1);
        keyboard.add(k2);
         //       KeyboardUtils.buttonOf("Расписание", "/schedule"),
         //       KeyboardUtils.buttonOf("Настройки", "/settings"),
         //       KeyboardUtils.buttonOf("Отмена", "/cancel"));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;

    }
}
