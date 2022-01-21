package com.hdpros.hdprosbackend.user.model;

import com.hdpros.hdprosbackend.bvn.model.BvnDetails;
import com.hdpros.hdprosbackend.image.model.ProfileImage;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String state;

    private String landMark;

    private String address;

    private boolean serviceProvider;

    @OneToOne(fetch = FetchType.LAZY)
    private BvnDetails bvnDetails;

    @OneToOne()
    private ProfileImage profileImage;

}
