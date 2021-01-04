package com.kakaopay.task.controller;

import com.kakaopay.task.dto.*;
import com.kakaopay.task.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

@RestController
@RequestMapping(value = "/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping(value = "/v1/payment")
    public ResponseEntity doPayment(@RequestBody PaymentRequest paymentRequest) {

        PaymentResponse response;
        try {
            response = paymentService.makePayment(paymentRequest);
        } catch (InvalidParameterException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/v1/payment/cancel")
    public ResponseEntity cancelAllPayment(@RequestBody CancelPaymentRequest cancelRequest) {

        CancelPaymentResponse cancelResponse;

        try {
            cancelResponse = paymentService.cancelAllPayment(cancelRequest);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(cancelResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/v1/payment/inquiry")
    public ResponseEntity getPaymentInfo(@RequestParam(value = "uuid") final String uuid) {

        PaymentInfoResponse response;

        try {
            response = paymentService.getPaymentInfo(uuid);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
