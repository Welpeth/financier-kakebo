package com.welpeth.kakebo.financier.domain.user.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.user.entity.QHolder;
import com.welpeth.kakebo.financier.domain.user.entity.Holder;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class HolderCustomRepositoryImpl extends BaseCustomRepositoryImpl<Holder> implements
    HolderCustomRepository {

  @Override
  public Optional<Holder> findUserByEmail(String email) {
    QHolder holder = QHolder.holder;

    return getQueryFactory()
        .select(holder)
        .from(holder)
        .where(holder.email.eq(email))
        .stream()
        .findFirst();
  }
}
