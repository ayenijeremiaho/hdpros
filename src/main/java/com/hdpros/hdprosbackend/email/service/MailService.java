package com.hdpros.hdprosbackend.email.service;

import java.util.Map;
import java.util.Objects;

public interface MailService {


    void sendMail(String mailSubject, String mailTo, String[] copy, Map<String, Object> parameters, String templateLocation);


    void sendMailAttachments(String mailSubject, String mailTo, String[] copy, Map<String, Object> parameters, String templateLocation, Map<String, Objects> attachements);


}
