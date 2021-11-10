package ru.lsan.pocketmanager.basepackage.database.entity;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Proxy(lazy = false)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "owner")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = true)
    private int telegram_id;

    @Column(nullable = true)
    private int message_id;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String timezone;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Event> events;

    @OneToOne(mappedBy = "owner")
    private CalendarEntity calendarEntity;

}
