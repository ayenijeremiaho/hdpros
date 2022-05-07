package com.hdpros.hdprosbackend.email.service.implementation;

import com.hdpros.hdprosbackend.email.model.Mail;
import com.hdpros.hdprosbackend.email.repository.MailRepository;
import com.hdpros.hdprosbackend.email.service.MailService;
import com.hdpros.hdprosbackend.utils.FileManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailServiceImpl implements MailService {


    @Value("${mail.from}")
    private String mailFrom;
    @Value("${mail.name}")
    private String mailName;

    @Autowired
    private MailRepository mailRepository;

    public JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.emailSender = mailSender;
    }


    @Override
    public void sendMail(String mailSubject, String mailTo, String[] copy, Map<String, Object> parameters, String templateLocation) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {

            @Override
            public void run() {

                Mail mail = new Mail();
                if (Objects.nonNull(copy)) mail.setCopy(Arrays.asList(copy));
                mail.setMailHeader(mailSubject);
                mail.setMailTo(mailTo);
                IContext context = new Context();
                ((Context) context).setVariables(parameters);
                mail.setMailContent(templateEngine.process("hdpros/" + templateLocation, context));
                logger.debug("MAIL -> {}", mail.toString());
                logger.info("Sending mail to ->{}", mail.getMailTo());
                MimeMessagePreparator messagePreparator = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
//                    messageHelper.setCc(copy);
                    messageHelper.setBcc(copy);
                    messageHelper.setFrom(mailFrom, mailName);
                    messageHelper.setTo(mailTo);
                    messageHelper.setSubject(mailSubject);
                    messageHelper.setText(mail.getMailContent(), true);
                };
                try {
                    mail.setLastSent(new Date());
                    emailSender.send(messagePreparator);
                    logger.info("Mail successfully sent");
                    mailRepository.save(mail);
                } catch (MailException e) {
                    //mail.setFailureReason(e.getMessage());
                    logger.error("Error sending mail", e);
                    //runtime exception; compiler will not force you to handle it
                }


            }
        });

    }

    @Override
    public void sendMailWithAttachment(String mailSubject, String mailTo, String[] copy, Map<String, Object> parameters, String templateLocation, String filePath) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {

            @Override
            public void run() {

                Mail mail = new Mail();
                if (Objects.nonNull(copy)) mail.setCopy(Arrays.asList(copy));
                mail.setMailHeader(mailSubject);
                mail.setMailTo(mailTo);
                IContext context = new Context();
                ((Context) context).setVariables(parameters);
                mail.setMailContent(templateEngine.process("hdpros/" + templateLocation, context));
                logger.debug("MAIL -> {}", mail);
                logger.info("Sending mail to ->{}", mail.getMailTo());
                MimeMessagePreparator messagePreparator = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                    messageHelper.setFrom(mailFrom, mailName);
                    messageHelper.setTo(mailTo);
                    messageHelper.setSubject(mailSubject);
                    messageHelper.setText(mail.getMailContent(), true);
                    if (Objects.nonNull(copy)) {
                        messageHelper.setCc(copy);
                    }

                    FileSystemResource file = new FileSystemResource(new File(filePath));
                    messageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
                };
                try {
                    mail.setLastSent(new Date());
                    emailSender.send(messagePreparator);
                    logger.info("Mail successfully sent");
                    mailRepository.save(mail);
                    FileManagement fileManagement = new FileManagement(filePath);
                    fileManagement.deleteFile();
                } catch (MailException e) {
                    //mail.setFailureReason(e.getMessage());
                    logger.error("Error sending mail", e);
                    //runtime exception; compiler will not force you to handle it
                }


            }
        });

    }

}
