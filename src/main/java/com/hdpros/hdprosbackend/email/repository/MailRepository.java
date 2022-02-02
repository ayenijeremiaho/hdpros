package com.hdpros.hdprosbackend.email.repository;

import com.hdpros.hdprosbackend.email.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {
}
