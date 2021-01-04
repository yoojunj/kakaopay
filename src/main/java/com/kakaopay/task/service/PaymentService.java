package com.kakaopay.task.service;

import com.kakaopay.task.dto.*;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest request);
    CancelPaymentResponse cancelAllPayment(CancelPaymentRequest request);
    PaymentInfoResponse getPaymentInfo(String request);
}
