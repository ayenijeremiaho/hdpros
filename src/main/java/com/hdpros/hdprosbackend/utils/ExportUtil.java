package com.hdpros.hdprosbackend.utils;

import com.hdpros.hdprosbackend.email.service.MailService;
import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

public class ExportUtil {

    private final MailService mailService;

    @Value("${mail.paymentAdmin}")
    private String adminMail;

    @Value("${institution.showAdditional.transaction.column}")
    private String showTransactionAdditionalColumn;

    @Value("${institution.showAdditional.walletTran.column}")
    private String showWalletTransactionAdditionalColumn;

    public ExportUtil(MailService mailService) {
        this.mailService = mailService;
    }

    public void createDirectory(String name) {
        FileManagement fileManagement = new FileManagement(name);
        fileManagement.createDirectory();
    }

    public void sendMail(User user, String fileName, String mailSubject, String reportDetails) {
        Map<String, Object> params = new HashMap<>();
        String firstname = user.getFirstName();

        params.put("firstName", firstname);
        params.put("username", user.getEmail());
        params.put("reportName", reportDetails);
        mailService.sendMailAttachments(mailSubject, user.getEmail(), null, params, "tran_report_template", fileName);
    }

    public void sendEODMail(String email, String institutionName, String fileName, String mailSubject, String reportDetails) {
        Map<String, Object> params = new HashMap<>();

        params.put("reportName", reportDetails);
        params.put("institutionName", institutionName);
        mailService.sendMailWithAttachment(mailSubject, email, null, params, "wallet_report_eod_template", null, fileName);
    }

    public void sendInstitutionSettlementMail(Institution institution, String fileName, Map<String, Object> params) {
        String[] copy = adminMail.split(",");

        String day = (String) params.get("transactionDay");
        mailService.sendMailWithAttachment("Settlement Report " + day, institution.getInstitutionEmail(), copy, params, "settlement_report_template", institution.getInstitutionID(), fileName);
    }

    public boolean showAdditionalColumnsForTransaction(Institution institution, String role) {
        if (Objects.isNull(institution) && role.equals("SUPER_ADMIN")) {
            return true;
        } else
            return getAsInstitutionList(showTransactionAdditionalColumn).contains(institution.getInstitutionID());
//            return (institution.getInstitutionID().equals("FREE444942") || institution.getInstitutionID().equals("CAPR453083"));
    }

    public boolean showAdditionalColumnsForWallet(Institution institution, String role) {
        if (Objects.isNull(institution) && role.equals("SUPER_ADMIN")) {
            return true;
        } else
            return getAsInstitutionList(showWalletTransactionAdditionalColumn).contains(institution.getInstitutionID());
//            return (institution.getInstitutionID().equals("FREE444942"));
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
