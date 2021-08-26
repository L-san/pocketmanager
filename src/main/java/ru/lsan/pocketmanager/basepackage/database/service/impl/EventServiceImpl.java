package ru.lsan.pocketmanager.basepackage.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lsan.pocketmanager.basepackage.bot.TelegramBot;
import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;
import ru.lsan.pocketmanager.basepackage.database.repository.EventRepository;
import ru.lsan.pocketmanager.basepackage.database.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TelegramBot bot;

    @Override
    public Event createEvent(String name, LocalDateTime date, Owner owner) {
        Event event = new Event();
        event.setName(name);
        event.setOwner(owner);
        event.setDate_time(date);
        return eventRepository.save(event);
    }

    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
    }

    @Override
    public Event getEventById(long id) {
        return eventRepository.getById(id);
    }

    @Override
    public List<Event> findAllByOwner(Owner owner) {
        return eventRepository.findAllByOwner(owner);
    }
}
