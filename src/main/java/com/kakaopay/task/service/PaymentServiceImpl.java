package com.kakaopay.task.service;

import com.kakaopay.task.common.ConfigUtils;
import com.kakaopay.task.common.CryptoUtils;
import com.kakaopay.task.dto.*;
import com.kakaopay.task.entity.Payment;
import com.kakaopay.task.entity.PaymentCancel;
import com.kakaopay.task.repository.PaymentCancelRepository;
import com.kakaopay.task.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.security.InvalidParameterException;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private int ENC_DATA_START = 83;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentCancelRepository paymentCancelRepository;


    @Override
    @Transactional
    public PaymentResponse makePayment(PaymentRequest request) {

        if(!request.isValid()){
            throw new InvalidParameterException();
        }

        String encData;
        try {
            CryptoUtils cryptoUtils = new CryptoUtils(ConfigUtils.encKey);
            encData = cryptoUtils.aesEncode(request.getConcatInfo());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        final String uuid = generateNewUUID();
        String paymentString = request.makePaymentString(uuid, encData);

        Payment payment = new Payment();
        payment.setUuid(uuid);
        payment.setAmount(request.getAmount());
        payment.setVat(request.getVat());
        payment.setForwardingData(paymentString);

        paymentRepository.save(payment);

        return PaymentResponse.builder()
                              .uuid(uuid)
                              .forwardedData(paymentString)
                              .build();
    }

    @Override
    @Transactional
    public CancelPaymentResponse cancelAllPayment(CancelPaymentRequest request) {

        Payment payment = paymentRepository.findByUuid(request.getUuid())
                                           .orElseThrow(() -> new EntityNotFoundException());

        if(payment.getPaymentCancelList() != null) {
            throw new IllegalStateException();
        }

        if(payment.getAmount().longValue() != request.getAmount().longValue()) {
            throw new InvalidParameterException("amount");
        }

        final String uuid = generateNewUUID();
        String forwardingData = payment.getForwardingData()
                                       .replace("PAYMENT", "CANCEL ")
                                       .replace(payment.getUuid(), uuid);
        String rearString = forwardingData.substring(ENC_DATA_START);
        rearString = payment.getUuid() + rearString.substring(20);

        forwardingData = forwardingData.substring(0, ENC_DATA_START) + rearString;

        PaymentCancel paymentCancel = new PaymentCancel();
        paymentCancel.setUuid(uuid);
        paymentCancel.setAmount(request.getAmount());
        paymentCancel.setVat(request.getVat());
        paymentCancel.setInstallmentPeriod("00");
        paymentCancel.setForwardingData(forwardingData);
        paymentCancel.setPayment(payment);

        paymentCancelRepository.save(paymentCancel);

        return CancelPaymentResponse.builder()
                                    .uuid(uuid)
                                    .forwardedData(forwardingData)
                                    .build();
    }

    @Override
    public PaymentInfoResponse getPaymentInfo(final String uuid) {


        Payment payment = paymentRepository.findByUuid(uuid)
                                           .orElse(null);

        String forwardingData = payment != null ? payment.getForwardingData() : null;
        Long amount = payment != null ? payment.getAmount() : 0L;
        Long vat    = payment != null ? payment.getVat() : 0L;

        if(payment == null) {
            PaymentCancel paymentCancel = paymentCancelRepository.findByUuid(uuid)
                                                                 .orElseThrow(() -> new EntityNotFoundException());

            forwardingData = paymentCancel.getForwardingData();

            amount  = paymentCancel.getAmount();
            vat     = paymentCancel.getVat();
        }

        String status = forwardingData.substring(4, 14)
                                      .trim();

        String encData = forwardingData.substring(ENC_DATA_START + 20)
                                       .trim();
        String decData;
        try {
            CryptoUtils cryptoUtils = new CryptoUtils(ConfigUtils.encKey);
            decData = cryptoUtils.aesDecode(encData);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String[] array = decData.split("#");
        String cardNumber = maskingCardNumber(array[0]);
        String validity = array[1];
        String cvc = array[2];


        return PaymentInfoResponse.builder()
                                  .uuid(uuid)
                                  .cardNumber(cardNumber)
                                  .validity(validity)
                                  .cvc(cvc)
                                  .status(status)
                                  .amount(amount)
                                  .vat(vat)
                                  .build();
    }

    private String generateNewUUID() {

        return UUID.randomUUID()
                   .toString()
                   .replace("-", "")
                   .substring(0, 20);
    }

    private String maskingCardNumber(String cardNumber) {

        String maskingCardNumber = cardNumber.substring(0, 6);

        for(int i = 6; i < cardNumber.length() - 3; i++) {
            maskingCardNumber += "*";
        }

        maskingCardNumber += cardNumber.substring(cardNumber.length() - 3);

        return maskingCardNumber;
    }
}
