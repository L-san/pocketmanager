package ru.lsan.pocketmanager.basepackage.database.service;
import ru.lsan.pocketmanager.basepackage.database.entity.CalendarEntity;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;

public interface OwnerService {

    void createOwner(int telegramId);

    void delete(Owner owner);

    Owner findByTelegramId(int telegram_id);

    void setStateAndUpdate(Owner owner, String state);

    void setZoneAndUpdate(Owner owner, String zone);

    void setMsgIdAndUpdate(Owner owner, int id);
}
