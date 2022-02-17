package com.hdpros.hdprosbackend;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hdpros.hdprosbackend.general.ConfigProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class HdprosBackendApplication {

    private final ConfigProperty configProperty;

    public HdprosBackendApplication(ConfigProperty configProperty) {
        this.configProperty = configProperty;
    }

    public static void main(String[] args) {
        SpringApplication.run(HdprosBackendApplication.class, args);
    }

    @Bean
    public Cloudinary getCloudinary() {
        // Set Cloudinary instance
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", configProperty.getCloudinaryCloudName(), // insert here you cloud name
                "api_key", configProperty.getCloudinaryApiKey(), // insert here your api code
                "api_secret", configProperty.getCloudinaryApiSecret())); // insert here your api secret
    }


}
