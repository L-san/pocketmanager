package ru.lsan.pocketmanager.basepackage.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.service.CalendarService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;

public class CalendarKeyboard {

    private static int prev = 0;
    private static int next = 0;
    private static int weekNum = 2;

    public static InlineKeyboardMarkup create(String callback, Owner owner,CalendarService calendarService) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(owner.getTimezone()));
        if (callback != null) {
            calendar.set(Calendar.MONTH, owner.getCalendarEntity().getMonth());
            if (callback.equals("prevMonth")) {
                rollMonth(calendar,-1);
                calendarService.setMonthAndUpdate(owner,calendar.get(Calendar.MONTH));
                prev = 1;
                if(next==1){
                    weekNum = 1;
                    prev = 0;
                    next = 0;
                }else{
                    weekNum = 2;
                }
            } else if (callback.equals("nextMonth")) {
                rollMonth(calendar,+1);
                calendarService.setMonthAndUpdate(owner,calendar.get(Calendar.MONTH));
                next = 1;
                if(prev==1){
                    weekNum = 2;
                    prev = 0;
                }else{
                    weekNum = 2;
                }
            }
            calendar = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
            calendar.set(Calendar.WEEK_OF_MONTH,1);
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

        ArrayList<InlineKeyboardButton> subList = new ArrayList<>();

        while (calendar.get(Calendar.MONTH)== initialMonth) {
            int[] daysOfWeek = getDaysOfWeek(calendar);
            for(Integer day:daysOfWeek){
                subList.add(KeyboardUtils.buttonOf(String.valueOf(day), String.valueOf(day)));
            }
            keyboard.add(subList);
            subList = new ArrayList<>();
        }
        return KeyboardUtils.inlineOfFromArray(keyboard);
    }

    private static int[] getDaysOfWeek(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_WEEK, weekNum);
        int[] daysOfWeek = new int[7];
        for (int i = 0; i < 7; i++) {
            daysOfWeek[i] = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }

    public static void rollMonth(Calendar calendar, int amount){
        int initialMonth = calendar.get(Calendar.MONTH);
        while (calendar.get(Calendar.MONTH)== initialMonth) {
            calendar.add(Calendar.DAY_OF_MONTH, amount);
        }
        if(amount<0){
            weekNum=1;
        }
    }
}
