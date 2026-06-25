package com.welpeth.kakebo.financier.domain.holderAddress.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.address.entity.Address;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.holderAddress.dto.CreateHolderAddressRequest;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddress;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddressId;
import com.welpeth.kakebo.financier.domain.holderAddress.repository.UserAddressRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAddressServiceTest {

  @Mock
  private UserAddressRepository repository;

  @InjectMocks
  private UserAddressService service;

  @Test
  @DisplayName("create mapeia holder e address e persiste o vínculo")
  void createMapsFieldsAndSaves() {
    // Arrange
    Holder holder = new Holder();
    Address address = new Address();
    CreateHolderAddressRequest request = new CreateHolderAddressRequest(holder, address);

    // Act
    HolderAddress created = service.create(request);

    // Assert
    assertThat(created.getHolder()).isSameAs(holder);
    assertThat(created.getAddress()).isSameAs(address);
    verify(repository).save(created);
  }

  @Test
  @DisplayName("get monta a chave composta e delega ao repositório")
  void getBuildsCompositeKeyAndDelegates() {
    // Arrange
    UUID idHolder = UUID.randomUUID();
    UUID idAddress = UUID.randomUUID();
    HolderAddress holderAddress = new HolderAddress();
    when(repository.getReferenceById(new HolderAddressId(idHolder, idAddress)))
        .thenReturn(holderAddress);

    // Act
    HolderAddress result = service.get(idHolder, idAddress);

    // Assert
    assertThat(result).isSameAs(holderAddress);
  }

  @Test
  @DisplayName("delete monta a chave composta e delega ao repositório")
  void deleteBuildsCompositeKeyAndDelegates() {
    // Arrange
    UUID idHolder = UUID.randomUUID();
    UUID idAddress = UUID.randomUUID();

    // Act
    service.delete(idHolder, idAddress);

    // Assert
    verify(repository).deleteById(new HolderAddressId(idHolder, idAddress));
  }

  @Test
  @DisplayName("getList retorna todos os vínculos")
  void getListReturnsAll() {
    // Arrange
    List<HolderAddress> links = List.of(new HolderAddress(), new HolderAddress());
    when(repository.findAll()).thenReturn(links);

    // Act
    List<HolderAddress> result = service.getList();

    // Assert
    assertThat(result).isSameAs(links);
  }
}
