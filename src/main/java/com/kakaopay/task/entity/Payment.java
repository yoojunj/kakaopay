package com.kakaopay.task.entity;

import com.kakaopay.task.common.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    @Column(name = "uuid", unique = true, length = 20)
    private String  uuid;
    private Long    amount;
    private Long    vat;
    @Column(length = 2)
    private String  installmentPeriod = "00";
    @Column(name = "forwarding_data", length = 450)
    private String  forwardingData;

    @OneToMany(mappedBy = "id")
    private List<PaymentCancel> paymentCancelList;

}
