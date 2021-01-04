package com.kakaopay.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PaymentInfoResponse {

    private String  uuid;
    private String  cardNumber;
    private String  validity;
    private String  cvc;
    private String  status;
    private Long    amount;
    private Long    vat;
}
