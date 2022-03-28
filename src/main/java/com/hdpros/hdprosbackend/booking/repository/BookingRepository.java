package com.hdpros.hdprosbackend.booking.repository;

import com.hdpros.hdprosbackend.booking.model.Booking;
import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByDescriptionAndUserAndDelFlag(String description, User user, boolean delFlag);

    int countAllByUserAndDelFlag(User user, boolean delFlag);

    List<Booking> findByUserAndDelFlag(User user, boolean delFlag);

    Booking findByUserAndIdAndDelFlag(User user, Long id, boolean delFlag);

    List<Booking> findByUserAndDelFlagAndJobStatus(User user, boolean delFlag, boolean jobStatus);

    List<Booking> findByUserAndDelFlagAndAccepted(User user, boolean delFlag, boolean accepted);

    List<Booking> findByUserAndDelFlagAndPaid(User user, boolean delFlag, boolean paid);
}
