package com.hdpros.hdprosbackend.booking.repository;

import com.hdpros.hdprosbackend.booking.model.Booking;
import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByDescriptionAndUserAndDelFlag(String description, User user, boolean delFlag);

    boolean existsByPaymentReference(String paymentReference);

    boolean existsByTransferReference(String transferReference);

    int countAllByUserAndDelFlag(User user, boolean delFlag);

    List<Booking> findByUserAndDelFlagOrderByCreatedAtDesc(User user, boolean delFlag);

    Booking findByUserAndIdAndDelFlag(User user, Long id, boolean delFlag);

    Booking findByIdAndDelFlag(Long id, boolean delFlag);

    List<Booking> findByUserAndDelFlagAndJobStatusAndPaidAndAcceptedOrderByCreatedAtDesc(User user, boolean delFlag, boolean jobStatus, boolean paid, boolean accepted);

    List<Booking> findByJobStatusAndPaidAndAcceptedAndDelFlagOrderByCreatedAtDesc(boolean jobStatus, boolean paid, boolean accepted, boolean delFlag);

    List<Booking> findByJobStatusAndPaidAndAcceptedAndDelFlagAndProviderIdOrderByCreatedAtDesc(boolean jobStatus, boolean paid, boolean accepted, boolean delFlag, Long providerId);

    List<Booking> findByProviderIdAndDelFlagOrderByCreatedAtDesc(Long providerId, boolean delFlag);
}
