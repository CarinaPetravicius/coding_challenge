package com.challenge.coding_challenge.service.gateway.database.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_line")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreditLineDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "founding_type")
    private FoundingTypeDB foundingType;

    @Column(name = "cash_balance")
    private Double cashBalance;

    @Column(name = "monthly_revenue")
    private Double monthlyRevenue;

    @Column(name = "requested_credit_line")
    private Double requestedCreditLine;

    @Column(name = "requested_date")
    private LocalDateTime requestedDate;

    @Column(name = "credit_line_recommended")
    private Double creditLineRecommended;

    @Column()
    private Boolean accepted;

    @Column(name = "credit_line_authorized")
    private Double creditLineAuthorized;

    @Column()
    private String message;

}