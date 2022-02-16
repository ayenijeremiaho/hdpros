package com.hdpros.hdprosbackend.user.Repository;

import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmailAndDelFlag(String email, boolean delFlag);

}