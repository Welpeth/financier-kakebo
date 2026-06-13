package com.welpeth.kakebo.financier.domain.account.repository;

import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, AccountCustomRepository {

  List<Account> findByHolder(Holder holder);
}
