package com.welpeth.kakebo.financier.domain.holder.repository;

import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolderRepository extends JpaRepository<Holder, UUID>, HolderCustomRepository {

}
