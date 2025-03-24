package com.Advocacia.ServiceTest;

import com.Advocacia.Entity.Despesa;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Repository.DespesaRepository;
import com.Advocacia.Service.DespesaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class DespesaServiceTest {
/*
    @InjectMocks
    private DespesaService despesaService;

    @Mock
    private DespesaRepository despesaRepository;

    private Despesa despesa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        despesa = new Despesa();
        despesa.setId(1L);
        despesa.setHonorario(new BigDecimal("1500.00"));
        despesa.setFormaPagamento("Cartão de Crédito");
        despesa.setStatusPagamento(StatusPagamento.PENDENTE);
        despesa.setDataVencimento(LocalDate.now().plusDays(30));
        despesa.setDespesasAdicionais(new BigDecimal("100.00"));
    }

    @Test
    void testSaveDespesa() {
        Mockito.when(despesaRepository.save(any(Despesa.class))).thenReturn(despesa);

        Despesa savedDespesa = despesaService.save(despesa);

        assertNotNull(savedDespesa);
        assertEquals(1L, savedDespesa.getId());
        assertEquals("Cartão de Crédito", savedDespesa.getFormaPagamento());
    }

    @Test
    void testUpdateDespesa() {
        Mockito.when(despesaRepository.findById(anyLong())).thenReturn(Optional.of(despesa));
        Mockito.when(despesaRepository.save(any(Despesa.class))).thenReturn(despesa);

        Despesa updatedDespesa = despesaService.update(1L, despesa);

        assertNotNull(updatedDespesa);
        assertEquals(1L, updatedDespesa.getId());
    }

    @Test
    void testUpdateDespesaNotFound() {
        Mockito.when(despesaRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            despesaService.update(1L, despesa);
        });

        assertEquals("Financeiro não encontrado", exception.getMessage());
    }

    @Test
    void testDeleteDespesa() {
        Mockito.when(despesaRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> despesaService.delete(1L));

        Mockito.verify(despesaRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDespesaNotFound() {
        Mockito.when(despesaRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            despesaService.delete(1L);
        });

        assertEquals("Financeiro não encontrado", exception.getMessage());
    }

    @Test
    void testFindAll() {
        List<Despesa> despesaList = Collections.singletonList(despesa);
        Mockito.when(despesaRepository.findAll()).thenReturn(despesaList);

        List<Despesa> result = despesaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Mockito.when(despesaRepository.findById(anyLong())).thenReturn(Optional.of(despesa));

        Optional<Despesa> foundDespesa = despesaService.findById(1L);

        assertTrue(foundDespesa.isPresent());
        assertEquals(1L, foundDespesa.get().getId());
    }

    @Test
    void testFindByStatus() {
        Mockito.when(despesaRepository.findAllByStatusPagamento(any(StatusPagamento.class)))
                .thenReturn(Collections.singletonList(despesa));

        List<Despesa> foundDespesas = despesaService.findByStatus(StatusPagamento.PENDENTE);

        assertNotNull(foundDespesas);
        assertEquals(1, foundDespesas.size());
        assertEquals(StatusPagamento.PENDENTE, foundDespesas.get(0).getStatusPagamento());
    }

    @Test
    void testFindByPagamentoPendente() {
        Mockito.when(despesaRepository.findAllByStatusPagamento(StatusPagamento.PENDENTE))
                .thenReturn(Collections.singletonList(despesa));

        List<Despesa> pendingDespesas = despesaService.findByPagamentoPendente();

        assertNotNull(pendingDespesas);
        assertEquals(1, pendingDespesas.size());
        assertEquals(StatusPagamento.PENDENTE, pendingDespesas.get(0).getStatusPagamento());
    }

    @Test
    void testFindByVencimento() {
        LocalDate dataVencimento = LocalDate.now().plusDays(30);
        Mockito.when(despesaRepository.findByVencimento(any(StatusPagamento.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(despesa));

        List<Despesa> foundByVencimento = despesaService.findByVencimento(StatusPagamento.PENDENTE, dataVencimento);

        assertNotNull(foundByVencimento);
        assertEquals(1, foundByVencimento.size());
        assertEquals(StatusPagamento.PENDENTE, foundByVencimento.get(0).getStatusPagamento());
    }
    
    @Test
    void testFindCategorias() {
        List<String> categorias = Arrays.asList("Alimentação", "Saúde", "Transporte");

        when(despesaRepository.findCategorias()).thenReturn(categorias);

        List<String> result = despesaService.findCategorias();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(categorias, result);
    }
*/
}
