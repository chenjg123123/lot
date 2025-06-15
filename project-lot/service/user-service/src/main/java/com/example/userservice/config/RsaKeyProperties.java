package com.example.userservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rsa")
public class RsaKeyProperties {
    private String publicKey;
    private String privateKey;
}
