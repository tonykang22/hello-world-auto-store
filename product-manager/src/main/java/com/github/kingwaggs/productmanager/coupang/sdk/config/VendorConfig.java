package com.github.kingwaggs.productmanager.coupang.sdk.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

public class VendorConfig {

    private final static String VENDOR_CONFIG_FILE = "vendor.properties";
    private final static String VENDOR_CONFIG_DECRYPTED_FILE = "decrypted.vendor.properties";
    private final Properties properties = new Properties();

    public VendorConfig() {
        this(VENDOR_CONFIG_FILE);
//        this(VENDOR_CONFIG_DECRYPTED_FILE);
    }

    public VendorConfig(String path) {
        URL resource = getClass().getClassLoader().getResource(path);
        try (InputStream input = Objects.requireNonNull(resource).openStream()) {
            properties.load(input);
        } catch (IOException ioe) {
            System.err.println("Error when reading vendor.properties file! " + VENDOR_CONFIG_FILE);
            ioe.printStackTrace();
        }
    }

    public VendorConfig(String path, String tag) {
        File initialFile = new File(path);
        try (InputStream input = new FileInputStream(initialFile)) {
            properties.load(input);
        } catch (IOException ioe) {
            System.err.println("Error when reading vendor.properties file! " + VENDOR_CONFIG_FILE);
            ioe.printStackTrace();
        }
    }

    // get vendor-defined property value
    public String getValue(String key) {
        return properties.getProperty(key);
    }
}
