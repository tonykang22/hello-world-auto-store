package com.github.kingwaggs.csmanager.util;

import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSourceUtil {

    private final MessageSource messageSource;

    private static final String WELCOME_MESSAGE = "welcome.message";
    private static final String SHIPPING_MESSAGE = "shipping.message";
    private static final String TRACKING_VIEW_URL = "http://packing.warpex.com/api/warpexTrack?wbl=";

    public String getWelcomeMessage(Welcome welcome, Locale locale) {
        String[] content = {welcome.getCustomerName(), welcome.getOrderId(), welcome.getProductName()};
        return messageSource.getMessage(WELCOME_MESSAGE, content, locale);
    }

    public String getShippingMessage(Shipping shipping, Locale locale) {
        String[] content = {shipping.getCustomerName(), shipping.getOrderId(),
                shipping.getProductName(), TRACKING_VIEW_URL + shipping.getShippingInvoice()};
        return messageSource.getMessage(SHIPPING_MESSAGE, content, locale);
    }

}
