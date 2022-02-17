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

    @Value("${cloudinary.cloudName}")
    private String cloudinaryCloudName;

    @Value("${cloudinary.apiKey}")
    private String cloudinaryApiKey;

    @Value("${cloudinary.apiSecret}")
    private String cloudinaryApiSecret;
}
