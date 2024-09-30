package com.Advocacia.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.Advocacia.Controller.FinanceiroController;
import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Service.FinanceiroService;
import jakarta.persistence.EntityNotFoundException;

class FinanceiroControllerTest {

    @InjectMocks
    private FinanceiroController financeiroController;

    @Mock
    private FinanceiroService financeiroService;

    private Financeiro financeiro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        financeiro = new Financeiro();
    }

    @Test
    void testSaveFinanceiro() {
        when(financeiroService.save(any(Financeiro.class))).thenReturn(financeiro);

        ResponseEntity<Financeiro> response = financeiroController.save(financeiro);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(financeiro, response.getBody());
    }

    @Test
    void testUpdateFinanceiro() {
        when(financeiroService.update(anyLong(), any(Financeiro.class))).thenReturn(financeiro);

        ResponseEntity<Financeiro> response = financeiroController.update(1L, financeiro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(financeiro, response.getBody());
    }

    @Test
    void testUpdateFinanceiroNotFound() {
        when(financeiroService.update(anyLong(), any(Financeiro.class))).thenThrow(EntityNotFoundException.class);

        ResponseEntity<Financeiro> response = financeiroController.update(1L, financeiro);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteFinanceiro() {
        doNothing().when(financeiroService).delete(anyLong());

        ResponseEntity<Void> response = financeiroController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteFinanceiroNotFound() {
        doThrow(EntityNotFoundException.class).when(financeiroService).delete(anyLong());

        ResponseEntity<Void> response = financeiroController.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindAll() {
        List<Financeiro> financeiroList = Collections.singletonList(financeiro);
        when(financeiroService.findAll()).thenReturn(financeiroList);

        ResponseEntity<List<Financeiro>> response = financeiroController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(financeiroList, response.getBody());
    }

    @Test
    void testFindById() {
        when(financeiroService.findById(anyLong())).thenReturn(Optional.of(financeiro));

        ResponseEntity<Financeiro> response = financeiroController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(financeiro, response.getBody());
    }

    @Test
    void testFindByStatus() {
        when(financeiroService.findByStatus(any(StatusPagamento.class))).thenReturn(Collections.singletonList(financeiro));

        ResponseEntity<List<Financeiro>> response = financeiroController.findByStatus(StatusPagamento.PAGO);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(Collections.singletonList(financeiro), response.getBody());
    }

    @Test
    void testFindByStatusNotFound() {
        when(financeiroService.findByStatus(any(StatusPagamento.class))).thenThrow(new RuntimeException());

        ResponseEntity<List<Financeiro>> response = financeiroController.findByStatus(StatusPagamento.PAGO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindByPagamentoPendente() {
        when(financeiroService.findByPagamentoPendente()).thenReturn(Collections.singletonList(financeiro));

        ResponseEntity<List<Financeiro>> response = financeiroController.findByPagamentoPendente();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(Collections.singletonList(financeiro), response.getBody());
    }

    @Test
    void testFindByPagamentoPendenteNotFound() {
        when(financeiroService.findByPagamentoPendente()).thenThrow(new RuntimeException());

        ResponseEntity<List<Financeiro>> response = financeiroController.findByPagamentoPendente();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindByVencimento() {
        when(financeiroService.findByVencimento(any(StatusPagamento.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(financeiro));

        ResponseEntity<List<Financeiro>> response = financeiroController.findByVencimento(StatusPagamento.PAGO, LocalDate.now());

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(Collections.singletonList(financeiro), response.getBody());
    }

    @Test
    void testFindByVencimentoNotFound() {
        when(financeiroService.findByVencimento(any(StatusPagamento.class), any(LocalDate.class)))
                .thenThrow(new RuntimeException());

        ResponseEntity<List<Financeiro>> response = financeiroController.findByVencimento(StatusPagamento.PAGO, LocalDate.now());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
