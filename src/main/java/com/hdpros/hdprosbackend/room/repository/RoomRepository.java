package com.hdpros.hdprosbackend.room.repository;

import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByDescriptionAndUserAndDelFlag(String description, User user, boolean delFlag);

    int countAllByUserAndDelFlag(User user, boolean delFlag);

    List<Room> findByUserAndDelFlag(User user, boolean delFlag);

    Room findByUserAndIdAndDelFlag(User user, Long id, boolean delFlag);
}
