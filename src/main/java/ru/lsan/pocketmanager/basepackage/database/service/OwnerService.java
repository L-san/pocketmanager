package ru.lsan.pocketmanager.basepackage.database.service;
import org.springframework.data.repository.query.Param;
import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OwnerService {

    void createOwner(int telegramId);

    void delete(Owner owner);

    Owner findByTelegramId(int telegram_id);

    void setStateAndUpdate(Owner owner, String state);

    void setZoneAndUpdate(Owner owner, String zone);

    void setTimeAndUpdate(Owner owner, Time time);
}
