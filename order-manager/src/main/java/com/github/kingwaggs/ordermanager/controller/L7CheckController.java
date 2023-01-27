package com.github.kingwaggs.ordermanager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/${l7-check.module}/monitor/l7check")
public class L7CheckController {

    @Value("${l7-check.module}")
    String module;

    boolean status = true;

    @GetMapping
    public ResponseEntity<?> getL7Check() {

        ResponseEntity<?> l7Status = getL7Status();

        if (!l7Status.getStatusCode().is2xxSuccessful()) {
            log.error("l7check status : {}", l7Status.getStatusCode());
        }

        return l7Status;
    }

    @GetMapping(value = "/enable")
    public ResponseEntity<?> setEnableL7Check() {
        setL7Check(true);
        log.info("l7check is enabled");
        return getL7Status();
    }

    @GetMapping(value = "/disable")
    public ResponseEntity<?> setDisableL7Check() {
        setL7Check(false);
        log.info("l7check is disabled");
        return getL7Status();
    }


    private ResponseEntity<?> getL7Status() {
        if (status) {
            return ResponseEntity.ok(String.format("l7check is working...(module : %s)", module));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void setL7Check(boolean newStatus) {
        status = newStatus;
    }
}
