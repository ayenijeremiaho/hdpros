package com.hdpros.hdprosbackend.places.model;

import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.utils.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String state;

    private String landMark;

    private String address;

    private String description;

    @ManyToOne(optional = false)
    private User user;

    private boolean delFlag = false;
}
