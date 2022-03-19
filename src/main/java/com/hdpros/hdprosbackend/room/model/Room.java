package com.hdpros.hdprosbackend.room.model;

import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.utils.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    private int count;

    private double price;

    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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
