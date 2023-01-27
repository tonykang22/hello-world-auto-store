package com.github.kingwaggs.csmanager.service.listener;

import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;

public interface MessageListener {

    void handleWelcome(Welcome welcome);

    void handleShipping(Shipping shipping);

}
