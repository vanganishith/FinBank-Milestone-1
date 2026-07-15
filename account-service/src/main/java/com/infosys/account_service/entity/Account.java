package com.infosys.account_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    private Integer accId;
    private Integer custId;      // links to a customer in Customer Service
    private String accType;      // SAVINGS, CURRENT
    private Double balance;

    @Transient  // not persisted in DB, filled in dynamically via Feign call
    private Customer customer;
}