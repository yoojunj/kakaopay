package com.kakaopay.task.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @CreationTimestamp
    private LocalDateTime   insertedAt;
    @UpdateTimestamp
    private LocalDateTime   updatedAt;
}
