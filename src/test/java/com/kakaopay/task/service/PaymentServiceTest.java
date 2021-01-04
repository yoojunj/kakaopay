package com.kakaopay.task.service;

import com.kakaopay.task.dto.*;
import com.kakaopay.task.repository.PaymentCancelRepository;
import com.kakaopay.task.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({PaymentServiceImpl.class})
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentCancelRepository paymentCancelRepository;

    private String paymentUUID = "0123456789abcdefghij";

    @Test
    @DisplayName("결제 성공 테스트")
    public void paymentSuccessTest() {

        // Given
        PaymentRequest paymentRequest = PaymentRequest.builder()
                                                      .cardNumber("1020304050607080")
                                                      .amount(10000L)
                                                      .cvc("123")
                                                      .installmentPeriod(0)
                                                      .validity("0123")
                                                      .build();

        // When
        PaymentResponse paymentResponse = paymentService.makePayment(paymentRequest);

        // Then
        assertTrue(paymentResponse != null);
        assertEquals(20, paymentResponse.getUuid().length());
        assertEquals(450, paymentResponse.getForwardedData().length());
    }

    @Test
    @DisplayName("결제 취소 성공테스트")
    public void paymentCancelSuccessTest() {

        // Given
        PaymentRequest paymentRequest = PaymentRequest.builder()
                                                      .cardNumber("1020304050607080")
                                                      .amount(10000L)
                                                      .cvc("123")
                                                      .installmentPeriod(0)
                                                      .validity("0123")
                                                      .build();

        PaymentResponse paymentResponse = paymentService.makePayment(paymentRequest);

        CancelPaymentRequest cancelPaymentRequest = CancelPaymentRequest.builder()
                                                                        .uuid(paymentResponse.getUuid())
                                                                        .amount(10000L)
                                                                        .build();

        // When
        CancelPaymentResponse cancelPaymentResponse = paymentService.cancelAllPayment(cancelPaymentRequest);

        // Then
        assertTrue(cancelPaymentResponse != null);
        assertEquals(20, cancelPaymentResponse.getUuid().length());
        assertEquals(450, cancelPaymentResponse.getForwardedData().length());
        assertTrue(!paymentResponse.getUuid().equals(cancelPaymentResponse.getUuid()));

    }

    @Test
    @DisplayName("결제 정보 조회 성공테스트")
    public void getPaymentInfoSuccessTest() {

        // Given
        PaymentRequest paymentRequest = PaymentRequest.builder()
                                                      .cardNumber("1020304050607080")
                                                      .amount(10000L)
                                                      .cvc("123")
                                                      .installmentPeriod(0)
                                                      .validity("0123")
                                                      .build();

        PaymentResponse paymentResponse = paymentService.makePayment(paymentRequest);

        // When
        PaymentInfoResponse response = paymentService.getPaymentInfo(paymentResponse.getUuid());

        // Then
        assertTrue(response != null);
        assertEquals(20, response.getUuid().length());
        assertEquals(10000L, response.getAmount().longValue());
        assertEquals(paymentRequest.getCardNumber().substring(0, 6), response.getCardNumber().substring(0, 6));
    }

    @Test
    @DisplayName("결제 취소 정보 조회 성공테스트")
    public void getCancelPaymentInfoSuccessTest() {

        // Given
        PaymentRequest paymentRequest = PaymentRequest.builder()
                                                      .cardNumber("1020304050607080")
                                                      .amount(10000L)
                                                      .cvc("123")
                                                      .installmentPeriod(0)
                                                      .validity("0123")
                                                      .build();

        PaymentResponse paymentResponse = paymentService.makePayment(paymentRequest);

        CancelPaymentRequest cancelPaymentRequest = CancelPaymentRequest.builder()
                                                                        .uuid(paymentResponse.getUuid())
                                                                        .amount(10000L)
                                                                        .build();

        CancelPaymentResponse cancelPaymentResponse = paymentService.cancelAllPayment(cancelPaymentRequest);

        // When
        PaymentInfoResponse response = paymentService.getPaymentInfo(cancelPaymentResponse.getUuid());

        // Then
        assertTrue(response != null);
        assertEquals(20, response.getUuid().length());
        assertEquals(10000L, response.getAmount().longValue());
        assertEquals(paymentRequest.getCardNumber().substring(0, 6), response.getCardNumber().substring(0, 6));

    }

}
