package com.github.kingwaggs.csmanager.sdk.coupang.domain;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Hmac {

    private static final String HMAC_SHA_256 = "HmacSHA256";
    private static final String HMAC_SHA_1 = "HmacSHA1";

    public Hmac() {
    }

    public static String generate(String method, String uri, String secretKey, String accessKey) {
        String[] parts = uri.split("\\?");
        if (parts.length > 2) {
            throw new RuntimeException("incorrect uri format");
        } else {
            String path = parts[0];
            String query = "";
            if (parts.length == 2) {
                query = parts[1];
            }

            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyMMdd'T'HHmmss'Z'");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            String datetime = dateFormatGmt.format(new Date());
            String message = datetime + method + path + query;

            String signature;
            try {
                SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(Charset.forName("UTF-8")), "HmacSHA256");
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(signingKey);
                byte[] rawHmac = mac.doFinal(message.getBytes(Charset.forName("UTF-8")));
                signature = Hex.encodeHexString(rawHmac);
            } catch (GeneralSecurityException var14) {
                throw new IllegalArgumentException("Unexpected error while creating hash: " + var14.getMessage(), var14);
            }

            String result = String.format("CEA algorithm=%s, access-key=%s, signed-date=%s, signature=%s", "HmacSHA256", accessKey, datetime, signature);
            return result;
        }
    }
}
