package com.welpeth.kakebo.financier.domain.user.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.user.entity.Holder;import java.util.Optional;

interface HolderCustomRepository extends BaseCustomRepository<Holder> {

Optional<Holder> findUserByEmail(String email);

}
