package com.hdpros.hdprosbackend.places.model;

import com.hdpros.hdprosbackend.user.model.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String state;

    private String landMark;

    private String address;

    private String description;

    @OneToOne(optional = false)
    private User user;

    private boolean delFlag = false;
}
