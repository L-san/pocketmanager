package ru.lsan.pocketmanager.basepackage.database.entity;

import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity
@Proxy(lazy = false)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calendar")
public class CalendarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "month")
    private int month;

    @Column(name = "day")
    private int day;

    @Column(name = "time")
    private String time;

    @Column(name = "prev")
    private Integer prev;

    @Column(name = "next")
    private Integer next;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
}
