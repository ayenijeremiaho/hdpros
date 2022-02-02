package com.hdpros.hdprosbackend.user.model;

import com.cloudinary.utils.ObjectUtils;
import com.hdpros.hdprosbackend.bvn.model.BvnDetails;
import com.hdpros.hdprosbackend.image.model.Image;
import lombok.Data;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@Entity(name = "users")
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

    @OneToMany(fetch = FetchType.LAZY)
    private List<Image> images;

    public void setImages(Image image) {
        this.images = Collections.singletonList(image);
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
