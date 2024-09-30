package com.Advocacia.ServiceTest;

import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Repository.FinanceiroRepository;
import com.Advocacia.Service.FinanceiroService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class FinanceiroServiceTest {

    @InjectMocks
    private FinanceiroService financeiroService;

    @Mock
    private FinanceiroRepository financeiroRepository;

    private Financeiro financeiro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        financeiro = new Financeiro();
        financeiro.setId(1L);
        financeiro.setHonorado(new BigDecimal("1500.00"));
        financeiro.setFormaPagamento("Cartão de Crédito");
        financeiro.setStatusPagamento(StatusPagamento.PENDENTE);
        financeiro.setDataVencimentoParcelas(LocalDate.now().plusDays(30));
        financeiro.setHistoricoPagamentos(Collections.singletonList(LocalDate.now()));
        financeiro.setDespesasAdicionais(new BigDecimal("100.00"));
    }

    @Test
    void testSaveFinanceiro() {
        Mockito.when(financeiroRepository.save(any(Financeiro.class))).thenReturn(financeiro);

        Financeiro savedFinanceiro = financeiroService.save(financeiro);

        assertNotNull(savedFinanceiro);
        assertEquals(1L, savedFinanceiro.getId());
        assertEquals("Cartão de Crédito", savedFinanceiro.getFormaPagamento());
    }

    @Test
    void testUpdateFinanceiro() {
        Mockito.when(financeiroRepository.findById(anyLong())).thenReturn(Optional.of(financeiro));
        Mockito.when(financeiroRepository.save(any(Financeiro.class))).thenReturn(financeiro);

        Financeiro updatedFinanceiro = financeiroService.update(1L, financeiro);

        assertNotNull(updatedFinanceiro);
        assertEquals(1L, updatedFinanceiro.getId());
    }

    @Test
    void testUpdateFinanceiroNotFound() {
        Mockito.when(financeiroRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            financeiroService.update(1L, financeiro);
        });

        assertEquals("Financeiro não encontrado", exception.getMessage());
    }

    @Test
    void testDeleteFinanceiro() {
        Mockito.when(financeiroRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> financeiroService.delete(1L));

        Mockito.verify(financeiroRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteFinanceiroNotFound() {
        Mockito.when(financeiroRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            financeiroService.delete(1L);
        });

        assertEquals("Financeiro não encontrado", exception.getMessage());
    }

    @Test
    void testFindAll() {
        List<Financeiro> financeiroList = Collections.singletonList(financeiro);
        Mockito.when(financeiroRepository.findAll()).thenReturn(financeiroList);

        List<Financeiro> result = financeiroService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }


    @Test
    void testFindById() {
        Mockito.when(financeiroRepository.findById(anyLong())).thenReturn(Optional.of(financeiro));

        Optional<Financeiro> foundFinanceiro = financeiroService.findById(1L);

        assertTrue(foundFinanceiro.isPresent());
        assertEquals(1L, foundFinanceiro.get().getId());
    }

    @Test
    void testFindByStatus() {
        Mockito.when(financeiroRepository.findAllByStatusPagamento(any(StatusPagamento.class)))
                .thenReturn(Collections.singletonList(financeiro));

        List<Financeiro> foundFinanceiros = financeiroService.findByStatus(StatusPagamento.PENDENTE);

        assertNotNull(foundFinanceiros);
        assertEquals(1, foundFinanceiros.size());
        assertEquals(StatusPagamento.PENDENTE, foundFinanceiros.get(0).getStatusPagamento());
    }

    @Test
    void testFindByPagamentoPendente() {
        Mockito.when(financeiroRepository.findAllByStatusPagamento(StatusPagamento.PENDENTE))
                .thenReturn(Collections.singletonList(financeiro));

        List<Financeiro> pendingFinanceiros = financeiroService.findByPagamentoPendente();

        assertNotNull(pendingFinanceiros);
        assertEquals(1, pendingFinanceiros.size());
        assertEquals(StatusPagamento.PENDENTE, pendingFinanceiros.get(0).getStatusPagamento());
    }

    @Test
    void testFindByVencimento() {
        LocalDate dataVencimento = LocalDate.now().plusDays(30);
        Mockito.when(financeiroRepository.findByVencimento(any(StatusPagamento.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(financeiro));

        List<Financeiro> foundByVencimento = financeiroService.findByVencimento(StatusPagamento.PENDENTE, dataVencimento);

        assertNotNull(foundByVencimento);
        assertEquals(1, foundByVencimento.size());
        assertEquals(StatusPagamento.PENDENTE, foundByVencimento.get(0).getStatusPagamento());
    }
}
