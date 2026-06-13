package com.welpeth.kakebo.financier.domain.ledgerEntry.repository;

import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID>, LedgerEntryCustomRepository {

}
