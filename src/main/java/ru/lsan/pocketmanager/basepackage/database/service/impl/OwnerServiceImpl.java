package ru.lsan.pocketmanager.basepackage.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lsan.pocketmanager.basepackage.database.entity.CalendarEntity;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.repository.OwnerRepository;
import ru.lsan.pocketmanager.basepackage.database.service.CalendarService;
import ru.lsan.pocketmanager.basepackage.database.service.OwnerService;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CalendarService calendarService;

    @Override
    public void delete(Owner owner) {
        ownerRepository.deleteById(owner.getId());
    }

    @Override
    public void createOwner(int telegramId) {
        Owner owner = new Owner();
        owner.setTelegram_id(telegramId);
        owner.setState("/start");
        ownerRepository.save(owner);
    }

    @Override
    public void setStateAndUpdate(Owner owner, String state) {
        Owner newOwner = Owner.builder()
                .id(owner.getId())
                .telegram_id(owner.getTelegram_id())
                .state(state)
                .timezone(owner.getTimezone())
                .message_id(owner.getMessage_id())
                .calendarEntity(owner.getCalendarEntity())
                .build();
       ownerRepository.save(newOwner);
    }

    @Override
    public void setZoneAndUpdate(Owner owner, String zone) {
        Owner newOwner = Owner.builder()
                .id(owner.getId())
                .telegram_id(owner.getTelegram_id())
                .state(owner.getState())
                .timezone(zone)
                .message_id(owner.getMessage_id())
                .calendarEntity(owner.getCalendarEntity())
                .build();
        ownerRepository.save(newOwner);
    }

    @Override
    public void setMsgIdAndUpdate(Owner owner, int id) {
        Owner newOwner = Owner.builder()
                .id(owner.getId())
                .telegram_id(owner.getTelegram_id())
                .state(owner.getState())
                .timezone(owner.getTimezone())
                .message_id(id)
                .calendarEntity(owner.getCalendarEntity())
                .build();
        ownerRepository.save(newOwner);
    }

    @Override
    public Owner findByTelegramId(int telegram_id) {
        return ownerRepository.findByTelegramId(telegram_id);
    }
}
