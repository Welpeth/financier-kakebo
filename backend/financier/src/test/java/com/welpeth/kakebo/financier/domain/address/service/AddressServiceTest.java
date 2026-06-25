package com.welpeth.kakebo.financier.domain.address.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.address.dto.CreateAddressRequest;
import com.welpeth.kakebo.financier.domain.address.entity.Address;
import com.welpeth.kakebo.financier.domain.address.repository.AddressRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

  @Mock
  private AddressRepository repository;

  @InjectMocks
  private AddressService service;

  @Test
  @DisplayName("create gera id e persiste o endereço")
  void createGeneratesIdAndSaves() {
    // Arrange
    CreateAddressRequest request = new CreateAddressRequest();

    // Act
    Address created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    verify(repository).save(created);
  }

  @Test
  @DisplayName("get retorna o endereço referenciado pelo id")
  void getReturnsReferencedAddress() {
    // Arrange
    UUID id = UUID.randomUUID();
    Address address = new Address();
    when(repository.getReferenceById(id)).thenReturn(address);

    // Act
    Address result = service.get(id);

    // Assert
    assertThat(result).isSameAs(address);
  }

  @Test
  @DisplayName("delete remove pelo id")
  void deleteRemovesById() {
    // Arrange
    UUID id = UUID.randomUUID();

    // Act
    service.delete(id);

    // Assert
    verify(repository).deleteById(id);
  }

  @Test
  @DisplayName("getList retorna todos os endereços")
  void getListReturnsAll() {
    // Arrange
    List<Address> addresses = List.of(new Address(), new Address());
    when(repository.findAll()).thenReturn(addresses);

    // Act
    List<Address> result = service.getList();

    // Assert
    assertThat(result).isSameAs(addresses);
  }

  @Test
  @DisplayName("create persiste exatamente o endereço criado")
  void createSavesTheCreatedEntity() {
    // Arrange
    CreateAddressRequest request = new CreateAddressRequest();

    // Act
    service.create(request);

    // Assert
    verify(repository).save(any(Address.class));
  }
}
