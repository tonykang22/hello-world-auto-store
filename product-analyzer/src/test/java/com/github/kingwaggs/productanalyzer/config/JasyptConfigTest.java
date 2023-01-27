package com.github.kingwaggs.productanalyzer.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class JasyptConfigTest {

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Value("${naver.openAPI.client.id}")
    private String naverOpenAPIClientId;

    @Value("${naver.openAPI.client.secret}")
    private String naverOpenAPIClientSecret;

    @Value("${naver.cloud.papago.client.id}")
    private String naverCloudPapagoClientId;

    @Value("${naver.cloud.papago.client.secret}")
    private String naverCloudPapagoClientSecret;

    @Value("${rainforest.API.key}")
    private String rainforestAPIKey;

    @Test
    void jasyptConfigTest() {
        // Arrange
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(password);

        // Act

        // Assert
        System.out.printf("(%s) before encryption : %s | after encryption : %s%n", "naverOpenAPIClientId", naverOpenAPIClientId, pbeEnc.encrypt(naverOpenAPIClientId));
        System.out.printf("(%s) before encryption : %s | after encryption : %s%n", "naverOpenAPIClientSecret", naverOpenAPIClientSecret, pbeEnc.encrypt(naverOpenAPIClientSecret));
        System.out.printf("(%s) before encryption : %s | after encryption : %s%n", "naverCloudPapagoClientId", naverCloudPapagoClientId, pbeEnc.encrypt(naverCloudPapagoClientId));
        System.out.printf("(%s) before encryption : %s | after encryption : %s%n", "naverCloudPapagoClientSecret", naverCloudPapagoClientSecret, pbeEnc.encrypt(naverCloudPapagoClientSecret));
        System.out.printf("(%s) before encryption : %s | after encryption : %s%n", "rainforestAPIKey", rainforestAPIKey, pbeEnc.encrypt(rainforestAPIKey));
    }
}