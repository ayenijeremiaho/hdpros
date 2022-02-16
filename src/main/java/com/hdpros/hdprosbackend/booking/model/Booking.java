package com.hdpros.hdprosbackend.booking.model;

import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.user.model.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobTitle;

    private LocalDate startDate;

    private LocalTime startTime;

    private String description;

    private int placeId;

    private boolean jobStatus;

    @OneToOne(optional = false)
    private User user;

    private boolean delFlag = false;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Room> rooms;

    public void setRooms(Room room) {
        this.rooms = Collections.singletonList(room);
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
