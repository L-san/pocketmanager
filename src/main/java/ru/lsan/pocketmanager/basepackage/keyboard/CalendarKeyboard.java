package ru.lsan.pocketmanager.basepackage.keyboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.CalendarService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;

public class CalendarKeyboard {

    public static InlineKeyboardMarkup create(String callback, Owner owner,CalendarService calendarService) {
        Calendar calendar = new GregorianCalendar();
        if (callback != null) {
            calendar.set(Calendar.MONTH, owner.getCalendarEntity().getMonth());
            if (callback.equals("prevMonth")) {
                calendar.roll(Calendar.MONTH, -1);
                calendarService.setMonthAndUpdate(owner,calendar.get(Calendar.MONTH));
            } else if (callback.equals("nextMonth")) {
                calendar.roll(Calendar.MONTH, +1);
                calendarService.setMonthAndUpdate(owner,calendar.get(Calendar.MONTH));
            }
            calendar = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        }

        DateFormat monthFormatter = new SimpleDateFormat("MMMM");
        String month = monthFormatter.format(calendar.getTime());
        int initialMonth = calendar.get(Calendar.MONTH);


        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        ArrayList<InlineKeyboardButton> subList1 = new ArrayList<>();
        subList1.add(KeyboardUtils.buttonOf("<<", "prevMonth"));
        subList1.add(KeyboardUtils.buttonOf(month, "monthVal"));
        subList1.add(KeyboardUtils.buttonOf(">>", "nextMonth"));
        keyboard.add(subList1);

        ArrayList<InlineKeyboardButton> subList2 = new ArrayList<>();
        subList2.add(KeyboardUtils.buttonOf("По", "Mon"));
        subList2.add(KeyboardUtils.buttonOf("Вт", "Tue"));
        subList2.add(KeyboardUtils.buttonOf("Ср", "Wed"));
        subList2.add(KeyboardUtils.buttonOf("Чт", "Thu"));
        subList2.add(KeyboardUtils.buttonOf("Пт", "Fri"));
        subList2.add(KeyboardUtils.buttonOf("Сб", "Sat"));
        subList2.add(KeyboardUtils.buttonOf("Вс", "Sun"));
        keyboard.add(subList2);

        int i = 0;
        ArrayList<InlineKeyboardButton> subList = new ArrayList<>();
        int currMonth = calendar.get(Calendar.MONTH);
        while (currMonth == initialMonth) {
            int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
            if (i == 0) {
                i++;
                while (dayNum != i + 1) {
                    subList.add(KeyboardUtils.buttonOf("  ", "  "));
                    i++;
                }
            }
            while ((currMonth == initialMonth) && (i <= 7)) {
                String dayMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
                calendar.roll(Calendar.DAY_OF_MONTH, +1);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                if ((day - Integer.parseInt(dayMonth)) < 0) {
                    subList.add(KeyboardUtils.buttonOf(dayMonth, dayMonth));
                    currMonth = calendar.get(Calendar.MONTH) + 1;
                    if (currMonth > 12) {
                        currMonth = 1;
                    }
                    if (weekDay != 1) {
                        for (int j = weekDay; j <= 7; j++) {
                            subList.add(KeyboardUtils.buttonOf("  ", "  "));
                        }
                    }
                } else {
                    subList.add(KeyboardUtils.buttonOf(dayMonth, dayMonth));
                }
                i++;
            }
            i = 1;
            keyboard.add(subList);
            subList = new ArrayList<>();
        }
        return KeyboardUtils.inlineOfFromArray(keyboard);
    }
}
