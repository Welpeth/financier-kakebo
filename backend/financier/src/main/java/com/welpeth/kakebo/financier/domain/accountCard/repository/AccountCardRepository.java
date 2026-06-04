package com.welpeth.kakebo.financier.domain.accountCard.repository;

import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountCardRepository extends JpaRepository<AccountCard, UUID> {

}
