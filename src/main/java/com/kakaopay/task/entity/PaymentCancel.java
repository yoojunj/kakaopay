package com.kakaopay.task.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@Entity
@Table(name = "payment")
public class PaymentCancel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    @Column(name = "uuid", unique = true, length = 20)
    private String  uuid;
    private Long    amount;
    private Long    vat;
    private String  installmentPeriod;
    @Column(name = "forwarding_data", length = 450)
    private String  forwardingData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

}
