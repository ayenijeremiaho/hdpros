package com.hdpros.hdprosbackend.image.repository;

import com.hdpros.hdprosbackend.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByUrlAndUser_Id(String url, Long user_id);

    Image findByUrlAndProfileImageAndUser_Id(String url, boolean profileImage, Long user_id);
}
