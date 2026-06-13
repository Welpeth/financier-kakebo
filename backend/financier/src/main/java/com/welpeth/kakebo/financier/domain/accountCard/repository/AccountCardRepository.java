package com.welpeth.kakebo.financier.domain.accountCard.repository;

import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCardRepository extends JpaRepository<AccountCard, UUID>, AccountCardCustomRepository {

}
