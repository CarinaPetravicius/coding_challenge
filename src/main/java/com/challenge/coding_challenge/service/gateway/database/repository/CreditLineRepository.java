package com.challenge.coding_challenge.service.gateway.database.repository;

import com.challenge.coding_challenge.service.gateway.database.model.CreditLineDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreditLineRepository extends JpaRepository<CreditLineDB, Long> {

    List<CreditLineDB> findAllByClientIdOrderByRequestedDateDesc(String clientId);

    List<CreditLineDB> findAllByClientIdAndAcceptedTrueAndRequestedDateGreaterThan(String clientId, LocalDateTime period);

}