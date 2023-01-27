package com.github.kingwaggs.csmanager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/${module.name}/monitor/l7check")
public class L7CheckController {

    @Value("${module.name}")
    private String module;

    private boolean status = true;

    @GetMapping
    public ResponseEntity<String> getL7Check() {
        ResponseEntity<String> l7Status = getL7Status();
        if (!l7Status.getStatusCode().is2xxSuccessful()) {
            log.error("l7check status : {}", l7Status.getStatusCode());
        }
        return l7Status;
    }

    @GetMapping(value = "/enable")
    public ResponseEntity<String> setEnableL7Check() {
        setL7Check(true);
        log.info("l7check is enabled");
        return getL7Status();
    }

    @GetMapping(value = "/disable")
    public ResponseEntity<String> setDisableL7Check() {
        setL7Check(false);
        log.info("l7check is disabled");
        return getL7Status();
    }


    private ResponseEntity<String> getL7Status() {
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
