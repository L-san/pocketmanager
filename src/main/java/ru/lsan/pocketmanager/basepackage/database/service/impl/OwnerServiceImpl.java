package ru.lsan.pocketmanager.basepackage.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.repository.OwnerRepository;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;


import java.sql.Time;
import java.util.*;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public void delete(Owner owner) {
        //ownerRepository.delete(owner);
        ownerRepository.deleteById(owner.getId());
    }

    @Override
    public void createOwner(int telegramId) {
        Owner owner = new Owner();
        owner.setTelegram_id(telegramId);
        owner.setState("/start");
        owner.setDaily_alert_time(null);
        ownerRepository.save(owner);

    }

    @Override
    public void setStateAndUpdate(Owner owner, String state) {
        Owner newOwner = Owner.builder()
                .id(owner.getId())
                .telegram_id(owner.getTelegram_id())
                .state(state)
                .daily_alert_time(owner.getDaily_alert_time())
                .timezone(owner.getTimezone())
                .build();
       ownerRepository.save(newOwner);
    }

    @Override
    public void setZoneAndUpdate(Owner owner, String zone) {
        Owner newOwner = Owner.builder()
                .id(owner.getId())
                .telegram_id(owner.getTelegram_id())
                .state(owner.getState())
                .daily_alert_time(owner.getDaily_alert_time())
                .timezone(zone)
                .build();
        ownerRepository.save(newOwner);
    }

    @Override
    public Owner findByTelegramId(int telegram_id) {
        return ownerRepository.findByTelegramId(telegram_id);
    }

    @Override
    public void setTimeAndUpdate(Owner owner, Time time) {
        Owner newOwner = Owner.builder()
                .id(owner.getId())
                .telegram_id(owner.getTelegram_id())
                .state(owner.getState())
                .daily_alert_time(time)
                .timezone(owner.getTimezone())
                .build();
        ownerRepository.save(newOwner);
    }
}
