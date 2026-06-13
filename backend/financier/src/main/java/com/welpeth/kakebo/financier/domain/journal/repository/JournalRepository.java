package com.welpeth.kakebo.financier.domain.journal.repository;

import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<Journal, UUID>, JournalCustomRepository {

}
