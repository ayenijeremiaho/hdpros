package com.hdpros.hdprosbackend;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class HdprosBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HdprosBackendApplication.class, args);
    }

    @Bean
    public Cloudinary getCloudinary() {
        // Set Cloudinary instance
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "xxx", // insert here you cloud name
                "api_key", "xxx", // insert here your api code
                "api_secret", "xxx")); // insert here your api secret
    }


}
