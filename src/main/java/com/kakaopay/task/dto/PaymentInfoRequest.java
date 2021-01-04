package com.kakaopay.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentInfoRequest {

    private String  uuid;
}
