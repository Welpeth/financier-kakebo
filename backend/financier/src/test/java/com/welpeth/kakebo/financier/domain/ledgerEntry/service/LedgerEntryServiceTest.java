package com.welpeth.kakebo.financier.domain.ledgerEntry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.journal.service.JournalService;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.CreateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.UpdateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import com.welpeth.kakebo.financier.domain.ledgerEntry.repository.LedgerEntryRepository;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import jakarta.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class LedgerEntryServiceTest {

  @Mock
  private LedgerEntryRepository repository;
  @Mock
  private JournalService journalService;

  @InjectMocks
  private LedgerEntryService service;

  @Test
  @DisplayName("create lança 400 quando o diário não é informado")
  void createRejectsMissingJournal() {
    // Arrange
    CreateLedgerEntryRequest request =
        new CreateLedgerEntryRequest("Lançamento", LocalDateTime.now(), null, new Transaction());

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.create(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ex.getReason()).isEqualTo("Diário obrigatório");
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("create lança 400 quando a transação não é informada")
  void createRejectsMissingTransaction() {
    // Arrange
    CreateLedgerEntryRequest request =
        new CreateLedgerEntryRequest("Lançamento", LocalDateTime.now(), new Journal(), null);

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.create(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ex.getReason()).isEqualTo("Transação obrigatória");
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("create válido persiste o lançamento e recalcula o total do diário")
  void createSavesAndRecalculatesJournalTotal() {
    // Arrange
    Journal journal = new Journal();
    journal.setId(UUID.randomUUID());
    CreateLedgerEntryRequest request =
        new CreateLedgerEntryRequest("Lançamento", LocalDateTime.now(), journal, new Transaction());

    // Act
    LedgerEntry created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo("Lançamento");
    assertThat(created.getJournal()).isSameAs(journal);
    verify(repository).save(created);
    verify(journalService).recalculateTotal(journal.getId());
  }

  @Test
  @DisplayName("get retorna o lançamento referenciado pelo id")
  void getReturnsReferencedEntry() {
    // Arrange
    UUID id = UUID.randomUUID();
    LedgerEntry entry = new LedgerEntry();
    when(repository.getReferenceById(id)).thenReturn(entry);

    // Act
    LedgerEntry result = service.get(id);

    // Assert
    assertThat(result).isSameAs(entry);
  }

  @Test
  @DisplayName("update delega ao repositório")
  void updateDelegatesToRepository() {
    // Arrange
    UpdateLedgerEntryRequest request =
        new UpdateLedgerEntryRequest(UUID.randomUUID(), "Atualizado", LocalDateTime.now());

    // Act
    service.update(request);

    // Assert
    verify(repository).update(request);
  }

  @Test
  @DisplayName("delete remove o lançamento e recalcula o total do diário associado")
  void deleteRemovesAndRecalculates() {
    // Arrange
    UUID id = UUID.randomUUID();
    Journal journal = new Journal();
    journal.setId(UUID.randomUUID());
    LedgerEntry entry = new LedgerEntry();
    entry.setJournal(journal);
    when(repository.findById(id)).thenReturn(Optional.of(entry));

    // Act
    service.delete(id);

    // Assert
    verify(repository).deleteById(id);
    verify(journalService).recalculateTotal(journal.getId());
  }

  @Test
  @DisplayName("delete lança NoResultException quando o lançamento não existe")
  void deleteThrowsWhenMissing() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());

    // Act
    NoResultException ex =
        assertThrows(NoResultException.class, () -> service.delete(id));

    // Assert
    assertThat(ex.getMessage()).isEqualTo("Registro não encontrado");
    verify(repository, never()).deleteById(any());
    verify(journalService, never()).recalculateTotal(any());
  }

  @Test
  @DisplayName("getList retorna todos os lançamentos")
  void getListReturnsAll() {
    // Arrange
    List<LedgerEntry> entries = List.of(new LedgerEntry(), new LedgerEntry());
    when(repository.findAll()).thenReturn(entries);

    // Act
    List<LedgerEntry> result = service.getList();

    // Assert
    assertThat(result).isSameAs(entries);
  }

  @Test
  @DisplayName("getListByJournal delega ao repositório com o id do diário")
  void getListByJournalDelegates() {
    // Arrange
    UUID journalId = UUID.randomUUID();
    List<LedgerEntry> entries = List.of(new LedgerEntry());
    when(repository.findByJournalId(journalId)).thenReturn(entries);

    // Act
    List<LedgerEntry> result = service.getListByJournal(journalId);

    // Assert
    assertThat(result).isSameAs(entries);
  }
}
