package ru.lsan.pocketmanager.basepackage.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.lsan.pocketmanager.basepackage.database.entity.Event;
import ru.lsan.pocketmanager.basepackage.database.entity.Owner;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Query("select b from Owner b where b.telegram_id = :telegram_id")
    Owner findByTelegramId(@Param("telegram_id") int telegram_id);
    //@Query("select b from Event b where b.owner_id = :owner_id")
    //Optional<Event> findAllEventsByTelegramId(@Param("telegram_id") int telegram_id);
}
