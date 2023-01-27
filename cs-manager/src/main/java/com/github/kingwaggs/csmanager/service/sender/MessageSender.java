package com.github.kingwaggs.csmanager.service.sender;

import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;

public interface MessageSender {

    void sendWelcomeAlarm(Welcome welcome);

    void sendShippingAlarm(Shipping shipping);

}
