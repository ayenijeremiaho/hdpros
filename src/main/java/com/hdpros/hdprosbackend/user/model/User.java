package com.hdpros.hdprosbackend.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hdpros.hdprosbackend.bvn.model.BvnDetails;
import com.hdpros.hdprosbackend.image.model.Image;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String state;

    private String landMark;

    private String address;

    private boolean serviceProvider;

    private boolean delFlag;

    @Column(name = "changePassword", columnDefinition = "boolean default false")
    private boolean changePassword;

    @OneToOne//(fetch = FetchType.LAZY)
    private BvnDetails bvnDetails;

    @OneToMany//(fetch = FetchType.LAZY)
    private List<Image> images;

    public void setImages(Image image) {
        List<Image> images = new ArrayList<>();
        images.add(image);
        setImages(images);
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
