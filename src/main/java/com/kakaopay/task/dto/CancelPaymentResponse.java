package com.kakaopay.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CancelPaymentResponse {

    private String  uuid;
    private String  forwardedData;
}
