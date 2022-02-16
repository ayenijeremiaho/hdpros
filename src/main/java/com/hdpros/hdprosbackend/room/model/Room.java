package com.hdpros.hdprosbackend.room.model;

import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.user.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    private int count;

    private double price;

    private String description;

    @OneToOne(optional = false)
    private User user;

    private boolean delFlag = false;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Image> images;

    public void setImages(Image image) {
        this.images = Collections.singletonList(image);
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
