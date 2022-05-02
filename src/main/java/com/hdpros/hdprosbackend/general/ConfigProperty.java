package com.hdpros.hdprosbackend.general;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ConfigProperty {

    @Value("${paystack.secretKey}")
    private String paystackSecret;

    @Value("${paystack.url}")
    private String paystackUrl;

    @Value("${paystack.bvn.url}")
    private String paystackBvnUrl;

    @Value("${paystack.bank.url}")
    private String paystackBankUrl;

    @Value("${paystack.verify.url}")
    private String paystackVerifyUrl;

    @Value("${paystack.recipient.url}")
    private String paystackRecipientUrl;

    @Value("${paystack.transfer.url}")
    private String paystackTransferUrl;

    @Value("${paystack.finalize.url}")
    private String paystackFinalizeTransferUrl;

    @Value("${paystack.verifyTransfer.url}")
    private String paystackVerifyTransferUrl;

    @Value("${cloudinary.cloudName}")
    private String cloudinaryCloudName;

    @Value("${cloudinary.apiKey}")
    private String cloudinaryApiKey;

    @Value("${cloudinary.apiSecret}")
    private String cloudinaryApiSecret;
}
