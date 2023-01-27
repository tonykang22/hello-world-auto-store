package com.github.kingwaggs.productanalyzerv2.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.profiles.active=local")
class JasyptTest {

    @Qualifier("jasyptStringEncryptor")
    @Autowired
    private StringEncryptor jasyptEncryptor;

    @Test @DisplayName("Jasypt 의도하는대로 동작")
    void decryptAsIntended() {
        // given
        String secret = "비밀";
        String encryptedSecret = jasyptEncryptor.encrypt(secret);

        // when
        String decryptedSecret = jasyptEncryptor.decrypt(encryptedSecret);

        // then
        assertEquals(secret, decryptedSecret);
    }

}