package com.github.kingwaggs.ordermanager.controller;

import com.github.kingwaggs.ordermanager.domain.dto.response.ZincResponse;
import com.github.kingwaggs.ordermanager.service.coupang.CoupangOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/order-manager/zinc")
@RequiredArgsConstructor
public class ZincWebhookController {

    private final CoupangOrderService coupangOrderService;

    private static final String OK = "ok";

    @PostMapping(path = "/request_succeeded")
    public ResponseEntity<String> postSucceededResponse(@RequestBody ZincResponse zincResponse) {
        coupangOrderService.updateZincSucceededRequest(zincResponse);
        return ResponseEntity.ok(OK);
    }

    @PostMapping(path = "/request_failed")
    public ResponseEntity<String> postFailedResponse(@RequestBody ZincResponse zincResponse) {
        coupangOrderService.updateZincFailedRequest(zincResponse);
        return ResponseEntity.ok(OK);
    }

    @PostMapping(path = "/tracking_obtained")
    public ResponseEntity<String> postTrackingNumber(@RequestBody ZincResponse zincResponse) {
        coupangOrderService.updateZincTrackingNumber(zincResponse);
        return ResponseEntity.ok(OK);
    }
}
