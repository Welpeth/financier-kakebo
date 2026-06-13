package com.welpeth.kakebo.financier.domain.holder.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import java.util.Optional;

interface HolderCustomRepository extends BaseCustomRepository<Holder> {

Optional<Holder> findUserByEmail(String email);

}
