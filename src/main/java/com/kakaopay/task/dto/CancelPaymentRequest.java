package com.kakaopay.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CancelPaymentRequest {

    private String  uuid;
    private Long    amount;
    @Builder.Default
    private Long    vat = 0L;


    public boolean isValid() {

        // check length
        if(    uuid.length() != 20
            || amount < 100L || amount > 1000000000L) {
            return false;
        }

        return true;
    }
}
