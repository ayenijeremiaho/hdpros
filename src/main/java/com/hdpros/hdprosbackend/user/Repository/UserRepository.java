package com.hdpros.hdprosbackend.user.Repository;

import com.hdpros.hdprosbackend.user.dto.UserResponse;
import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmailAndDelFlag(String email, boolean delFlag);

    boolean existsByEmailAndServiceProvider(String email, boolean service_provider);

    User findByIdAndDelFlag(Long id, boolean delFlag);

    UserResponse findByEmail(String email);

}