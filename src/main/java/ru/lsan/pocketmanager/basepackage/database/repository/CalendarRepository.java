package ru.lsan.pocketmanager.basepackage.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lsan.pocketmanager.basepackage.database.entity.CalendarEntity;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity,Long> {
}
