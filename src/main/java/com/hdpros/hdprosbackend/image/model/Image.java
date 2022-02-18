package com.hdpros.hdprosbackend.image.model;

import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.user.model.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String publicId;

    private boolean profileImage = false;

    private boolean roomImage = false;

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;

}
