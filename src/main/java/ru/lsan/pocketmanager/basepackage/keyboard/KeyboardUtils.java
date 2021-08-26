package ru.lsan.pocketmanager.basepackage.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardUtils {

    public static InlineKeyboardMarkup inlineOf(InlineKeyboardButton... buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> keyboard = Arrays.stream(buttons)
                .map(Collections::singletonList)
                .collect(Collectors.toList());

        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup inlineOfFromArray(List<List<InlineKeyboardButton>> keyboard ) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardButton buttonOf(String text, String callbackData) {
        final InlineKeyboardButton result = new InlineKeyboardButton();
        result.setText(text);
        result.setCallbackData(callbackData);
        return result;
    }

}
