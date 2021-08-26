package ru.lsan.pocketmanager.basepackage.keyboard;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TimeZoneKeyboard {

    public static InlineKeyboardMarkup create(){
        Set<String> allZones = ZoneId.getAvailableZoneIds();
        List<String> zoneList = new ArrayList<String>(allZones);
        Collections.sort(zoneList);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int i = 0;
        ArrayList<InlineKeyboardButton> subList = new ArrayList<>();
        for(String zone : zoneList){
            if(zone.startsWith("Europe")){
                if(i<4){
                    subList.add(KeyboardUtils.buttonOf(zone.split("Europe/")[1],zone));
                    i++;
                }else{
                    keyboard.add(subList);
                    i = 0;
                    subList = new ArrayList<>();
                }
            }
        }
        return KeyboardUtils.inlineOfFromArray(keyboard);
    }
}
