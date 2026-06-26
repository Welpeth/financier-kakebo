package com.welpeth.kakebo.financier.domain.journal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.journal.dto.CreateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.dto.UpdateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.journal.repository.JournalRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JournalServiceTest {

  @Mock
  private JournalRepository repository;

  @InjectMocks
  private JournalService service;

  @Test
  @DisplayName("create gera id, mapeia o nome e inicializa o total em zero")
  void createInitializesTotalToZero() {
    // Arrange
    CreateJournalRequest request = new CreateJournalRequest("Diário 2026");

    // Act
    Journal created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo("Diário 2026");
    assertThat(created.getTotalValue()).isEqualByComparingTo(BigDecimal.ZERO);
    verify(repository).save(created);
  }

  @Test
  @DisplayName("get retorna o diário referenciado pelo id")
  void getReturnsReferencedJournal() {
    // Arrange
    UUID id = UUID.randomUUID();
    Journal journal = new Journal();
    when(repository.getReferenceById(id)).thenReturn(journal);

    // Act
    Journal result = service.get(id);

    // Assert
    assertThat(result).isSameAs(journal);
  }

  @Test
  @DisplayName("update delega ao repositório")
  void updateDelegatesToRepository() {
    // Arrange
    UpdateJournalRequest request = new UpdateJournalRequest(UUID.randomUUID(), "Atualizado");

    // Act
    service.update(request);

    // Assert
    verify(repository).update(request);
  }

  @Test
  @DisplayName("recalculateTotal delega ao repositório com o id informado")
  void recalculateTotalDelegatesToRepository() {
    // Arrange
    UUID journalId = UUID.randomUUID();

    // Act
    service.recalculateTotal(journalId);

    // Assert
    verify(repository).recalculateTotal(journalId);
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
  @DisplayName("getList retorna todos os diários")
  void getListReturnsAll() {
    // Arrange
    List<Journal> journals = List.of(new Journal(), new Journal());
    when(repository.findAll()).thenReturn(journals);

    // Act
    List<Journal> result = service.getList();

    // Assert
    assertThat(result).isSameAs(journals);
  }
}
