package com.hdpros.hdprosbackend.image.model;

import com.hdpros.hdprosbackend.user.model.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String publicId;

    @OneToOne
    private User user;

}
