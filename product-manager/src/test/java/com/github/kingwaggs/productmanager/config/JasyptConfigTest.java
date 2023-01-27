package com.github.kingwaggs.productmanager.config;

import com.github.kingwaggs.productmanager.common.config.JasyptConfig;
import com.github.kingwaggs.productmanager.coupang.sdk.config.VendorConfig;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

    public static final String format = "(%s) encryption : %s | decryption : %s%n";
    public static final String EXCHANGE_RATE_API_KEY = "exchange.rate.koreaexim.api.key";
    public static final String COUPANG_VENDOR_ACCESS_KEY = "coupang.vendor.access.key";
    public static final String COUPANG_VENDOR_SECRET_KEY = "coupang.vendor.secret.key";
    public static final String COUPANG_VENDOR_ID = "coupang.vendor.id";
    public static final String COUPANG_VENDOR_USER_ID = "coupang.vendor.user.id";
    private final JasyptConfig jasyptConfig = new JasyptConfig();

    @Test
    public void jasyptConfigTest() {
        // Arrange
        VendorConfig appConfig = new VendorConfig("../backup/product-manager/vendor.properties");
        VendorConfig vendorConfig = new VendorConfig("../backup/product-manager/vendor.properties");
        StringEncryptor stringEncryptor = jasyptConfig.stringEncryptor();

        // Act

        // Assert
        String encryptExchangeRateKey = stringEncryptor.encrypt(appConfig.getValue(EXCHANGE_RATE_API_KEY));
        String decryptExchangeRateKey = stringEncryptor.decrypt(encryptExchangeRateKey);
        System.out.printf(format, EXCHANGE_RATE_API_KEY, encryptExchangeRateKey, decryptExchangeRateKey);

        String encryptCoupangVendorAccessKey = stringEncryptor.encrypt(vendorConfig.getValue(COUPANG_VENDOR_ACCESS_KEY));
        String decryptCoupangVendorAccessKey = stringEncryptor.decrypt(encryptCoupangVendorAccessKey);
        System.out.printf(format, COUPANG_VENDOR_ACCESS_KEY, encryptCoupangVendorAccessKey, decryptCoupangVendorAccessKey);

        String encryptCoupangVendorSecretKey = stringEncryptor.encrypt(vendorConfig.getValue(COUPANG_VENDOR_SECRET_KEY));
        String decryptCoupangVendorSecretKey = stringEncryptor.decrypt(encryptCoupangVendorSecretKey);
        System.out.printf(format, COUPANG_VENDOR_SECRET_KEY, encryptCoupangVendorSecretKey, decryptCoupangVendorSecretKey);

        String encryptCoupangVendorId = stringEncryptor.encrypt(vendorConfig.getValue(COUPANG_VENDOR_ID));
        String decryptCoupangVendorId = stringEncryptor.decrypt(encryptCoupangVendorId);
        System.out.printf(format, COUPANG_VENDOR_ID, encryptCoupangVendorId, decryptCoupangVendorId);

        String encryptCoupangVendorUserId = stringEncryptor.encrypt(vendorConfig.getValue(COUPANG_VENDOR_USER_ID));
        String decryptCoupangVendorUserId = stringEncryptor.decrypt(encryptCoupangVendorUserId);
        System.out.printf(format, COUPANG_VENDOR_USER_ID, encryptCoupangVendorUserId, decryptCoupangVendorUserId);
    }
}