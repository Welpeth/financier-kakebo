package com.welpeth.kakebo.financier.domain.account.repository;

import com.welpeth.kakebo.financier.domain.account.entity.Account;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {

}
