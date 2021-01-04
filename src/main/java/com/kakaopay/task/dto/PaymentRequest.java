package com.kakaopay.task.dto;

import com.kakaopay.task.common.CommonUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentRequest {

    private String  cardNumber;
    private String  validity;
    private String  cvc;
    @Builder.Default
    private int     installmentPeriod = 0;
    private Long    amount;
    @Builder.Default
    private Long    vat = 0L;


    public boolean isValid() {

        // check length
        if(    cardNumber.length() < 10 || cardNumber.length() > 16
            || validity.length() != 4
            || cvc.length() != 3
            || installmentPeriod < 0 || installmentPeriod > 12
            || amount < 100L || amount > 1000000000L
            || (vat != null && amount < vat) ) {
            return false;
        }

        // check type
        try {
            Long.parseLong(cardNumber);
            Integer.parseInt(validity);
            Integer.parseInt(cvc);
        } catch (Exception e) {
            return false;
        }

        calVat();

        return true;
    }

    private void calVat() {

        if(this.vat == null) {
            double d = this.amount / 11.0;
            this.vat = Math.round(d);
        }
    }

    public String getConcatInfo() {
        return this.cardNumber + "#" + this.getValidity() + "#" + this.getCvc();
    }

    public String makePaymentString(final String uuid, final String encData) {

        String paymentString = "PAYMENT" + CommonUtils.getRemainingSpaces("PAYMENT", 10)
                                + uuid
                                + this.cardNumber + CommonUtils.getRemainingSpaces(this.cardNumber, 20)
                                + CommonUtils.getRemainingSpaces(this.installmentPeriod+"", 2).replaceAll(" ", "0") + this.installmentPeriod
                                + this.validity
                                + this.cvc
                                + CommonUtils.getRemainingSpaces(this.amount+"", 10) + this.amount
                                + CommonUtils.getRemainingSpaces(this.vat+"", 10).replaceAll(" ", "0") + this.vat
                                + CommonUtils.getRemainingSpaces("", 20)
                                + encData + CommonUtils.getRemainingSpaces(encData, 300)
                                + CommonUtils.getRemainingSpaces("", 47);

        paymentString = CommonUtils.getRemainingSpaces(paymentString.length()+"", 4) + paymentString.length() + paymentString;

        return paymentString;
    }
}
