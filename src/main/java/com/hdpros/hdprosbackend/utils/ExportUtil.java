package com.hdpros.hdprosbackend.utils;

import com.hdpros.hdprosbackend.email.service.MailService;
import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExportUtil {

    private final MailService mailService;

    @Value("${mail.paymentAdmin}")
    private String adminMail;

    @Value("${mailing-list}")
    private String mailList;
//
//    @Value("${institution.showAdditional.transaction.column}")
//    private String showTransactionAdditionalColumn;
//
//    @Value("${institution.showAdditional.walletTran.column}")
//    private String showWalletTransactionAdditionalColumn;

    public ExportUtil(MailService mailService) {
        this.mailService = mailService;
    }

    public void createDirectory(String name) {
        FileManagement fileManagement = new FileManagement(name);
        fileManagement.createDirectory();
    }

    public void sendEODMail(String institutionName, String fileName, String mailSubject, String reportDetails) {
        String[] copy = mailList.split(",");

        Map<String, Object> params = new HashMap<>();

        params.put("reportName", reportDetails);
        params.put("institutionName", institutionName);
        mailService.sendMailWithAttachment(mailSubject, adminMail, copy, params, "transfer_transaction",  fileName);
    }

    public void deleteAllFilesInFolders(String... folderNames) {
        for (String folderName : folderNames) {
            FileManagement fileManagement = new FileManagement(folderName);
            fileManagement.deleteAllFileInFolder();
        }
    }

    private List<String> getAsInstitutionList(String stringValues) {
        return Arrays.stream(stringValues.split(",")).map(String::trim).collect(Collectors.toList());
    }

}
