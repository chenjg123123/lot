package com.example.userservice.util;

import com.example.service.exception.RSABusinessException;
import com.example.userservice.config.RsaKeyProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Component
public class RsaUtil {

    private final RsaKeyProperties keyProperties;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RsaUtil(RsaKeyProperties keyProperties) {
        this.keyProperties = keyProperties;
    }

    @PostConstruct
    public void init() {
        try {
            // 解析公钥
            String pubKeyPem = keyProperties.getPublicKey();
            byte[] pubKeyBytes = Base64.getDecoder().decode(pubKeyPem);
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
            publicKey = KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);

            // 解析私钥（PKCS#8 格式）
            String priKeyPem = keyProperties.getPrivateKey();
            byte[] priKeyBytes = Base64.getDecoder().decode(priKeyPem);
            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(priKeySpec);
        } catch (Exception e) {
            throw new RSABusinessException("初始化密钥失败：" + e.getMessage());
        }
    }

    // 私钥解密
    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RSABusinessException("解密失败：" + e.getMessage());
        }
    }

    // 公钥加密
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RSABusinessException("加密失败：" + e.getMessage());
        }
    }
}
