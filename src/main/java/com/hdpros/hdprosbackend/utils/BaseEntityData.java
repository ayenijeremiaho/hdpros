package com.hdpros.hdprosbackend.utils;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class BaseEntityData {

    private String createdAt;

    private String updatedAt;

}
