package ru.lsan.pocketmanager.basepackage.database.service;

import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventService {

    Event createEvent(String name, LocalDateTime date, Owner owner);

    void delete(Event event);

    Event getEventById(long id);

    List<Event> findAllByOwner(Owner owner);

}
