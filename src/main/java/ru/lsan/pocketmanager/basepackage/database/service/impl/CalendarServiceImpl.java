package ru.lsan.pocketmanager.basepackage.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lsan.pocketmanager.basepackage.database.entity.CalendarEntity;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.repository.CalendarRepository;
import ru.lsan.pocketmanager.basepackage.database.service.CalendarService;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Override
    public CalendarEntity create(Owner owner, int month) {
        CalendarEntity calendarEntity = new CalendarEntity();
        calendarEntity.setOwner(owner);
        calendarEntity.setMonth(month);
        calendarEntity.setPrev(0);
        calendarEntity.setNext(0);
        return calendarRepository.save(calendarEntity);
    }

    @Override
    public void setMonthAndUpdate(Owner owner, int month) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .id(owner.getCalendarEntity().getId())
                .owner(owner)
                .month(month)
                .day(owner.getCalendarEntity().getDay())
                .time(owner.getCalendarEntity().getTime())
                .next(owner.getCalendarEntity().getNext())
                .prev(owner.getCalendarEntity().getPrev())
                .build();
        calendarRepository.save(calendarEntity);
    }

    @Override
    public void setDayAndUpdate(Owner owner, int day) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .id(owner.getCalendarEntity().getId())
                .owner(owner)
                .month(owner.getCalendarEntity().getMonth())
                .day(day)
                .time(owner.getCalendarEntity().getTime())
                .next(owner.getCalendarEntity().getNext())
                .prev(owner.getCalendarEntity().getPrev())
                .build();
        calendarRepository.save(calendarEntity);
    }

    @Override
    public void setTimeAndUpdate(Owner owner, String time) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .id(owner.getCalendarEntity().getId())
                .owner(owner)
                .month(owner.getCalendarEntity().getMonth())
                .day(owner.getCalendarEntity().getDay())
                .time(time)
                .next(owner.getCalendarEntity().getNext())
                .prev(owner.getCalendarEntity().getPrev())
                .build();
        calendarRepository.save(calendarEntity);
    }

    @Override
    public void setPrevAndUpdate(Owner owner, int prev) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .id(owner.getCalendarEntity().getId())
                .owner(owner)
                .month(owner.getCalendarEntity().getMonth())
                .day(owner.getCalendarEntity().getDay())
                .time(owner.getCalendarEntity().getTime())
                .next(owner.getCalendarEntity().getNext())
                .prev(prev)
                .build();
        calendarRepository.save(calendarEntity);
    }

    @Override
    public void setNextAndUpdate(Owner owner, int next) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .id(owner.getCalendarEntity().getId())
                .owner(owner)
                .month(owner.getCalendarEntity().getMonth())
                .day(owner.getCalendarEntity().getDay())
                .time(owner.getCalendarEntity().getTime())
                .next(next)
                .prev(owner.getCalendarEntity().getPrev())
                .build();
        calendarRepository.save(calendarEntity);
    }

    @Override
    public int getPrev(Owner owner) {
        if (calendarRepository.getById(owner.getCalendarEntity().getId()).getPrev() == null) {
            return 0;
        } else {
            return calendarRepository.getById(owner.getCalendarEntity().getId()).getPrev();
        }
    }

    @Override
    public int getNext(Owner owner) {
        if(calendarRepository.getById(owner.getCalendarEntity().getId()).getNext()==null){
            return 0;
        }else{
            return calendarRepository.getById(owner.getCalendarEntity().getId()).getNext();
        }
    }

    @Override
    public CalendarEntity findById(Long id) {
        return calendarRepository.getById(id);
    }
}
