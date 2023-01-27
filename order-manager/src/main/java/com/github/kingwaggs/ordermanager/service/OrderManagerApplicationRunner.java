package com.github.kingwaggs.ordermanager.service;

import org.springframework.boot.ApplicationRunner;

public interface OrderManagerApplicationRunner extends ApplicationRunner {

    void syncOrders();
}
