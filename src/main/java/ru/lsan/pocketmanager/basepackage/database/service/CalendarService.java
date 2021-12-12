package ru.lsan.pocketmanager.basepackage.database.service;

import ru.lsan.pocketmanager.basepackage.database.entity.CalendarEntity;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;

public interface CalendarService {

    CalendarEntity create(Owner owner, int month);

    void setMonthAndUpdate(Owner owner, int month);

    void setDayAndUpdate(Owner owner, int day);

    void setTimeAndUpdate(Owner owner, String time);

    void setPrevAndUpdate(Owner owner, int prev);

    void setNextAndUpdate(Owner owner, int next);

    int getPrev(Owner owner);

    int getNext(Owner owner);

    CalendarEntity findById(Long id);

}
