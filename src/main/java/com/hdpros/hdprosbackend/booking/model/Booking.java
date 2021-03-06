package com.hdpros.hdprosbackend.booking.model;

import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.utils.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobTitle;

    private LocalDate startDate;

    private LocalTime startTime;

    private String description;

    private Long placeId;

    private boolean jobStatus = false;

    @Column(name = "accepted", columnDefinition = "boolean default false")
    private boolean accepted = false;

    @Column(name = "paid", columnDefinition = "boolean default false")
    private boolean paid = false;

    @Column(name = "in_progress", columnDefinition = "boolean default false")
    private boolean inProgress = false;

    @Column(name = "processing_payment", columnDefinition = "boolean default false")
    private boolean processingPayment = false;

    @Column(name = "completed", columnDefinition = "boolean default false")
    private boolean completed = false;

    @ManyToOne(optional = false)
    private User user;

    private Long providerId;

    private boolean delFlag = false;

    @Column(unique = true, nullable = true)
    private String paymentReference;

    private LocalDateTime paymentDate;

    @Column(unique = true, nullable = true)
    private String transferReference;

    private LocalDateTime transferDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Room> rooms;

    public void setRooms(Room room) {
        this.rooms = Collections.singletonList(room);
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
