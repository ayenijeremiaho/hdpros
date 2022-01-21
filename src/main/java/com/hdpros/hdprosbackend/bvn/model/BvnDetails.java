package com.hdpros.hdprosbackend.bvn.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BvnDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String bvn;

    private String bankCode;

    private String accountNumber;

    private String firstName;

    private String lastName;

    private String middleName;

    private boolean isBlacklisted = false;

    private String status;
}
