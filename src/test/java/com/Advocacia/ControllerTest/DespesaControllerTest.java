package com.Advocacia.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Arrays;
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
import com.Advocacia.Controller.DespesaController;
import com.Advocacia.Entity.Despesa;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Service.DespesaService;
import jakarta.persistence.EntityNotFoundException;

class DespesaControllerTest {

    @InjectMocks
    private DespesaController despesaController;

    @Mock
    private DespesaService despesaService;

    private Despesa despesa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        despesa = new Despesa();
    }

    @Test
    void testSaveDespesa() {
        when(despesaService.save(any(Despesa.class))).thenReturn(despesa);

        ResponseEntity<Despesa> response = despesaController.save(despesa);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(despesa, response.getBody());
    }

    @Test
    void testUpdateDespesa() {
        when(despesaService.update(anyLong(), any(Despesa.class))).thenReturn(despesa);

        ResponseEntity<Despesa> response = despesaController.update(1L, despesa);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(despesa, response.getBody());
    }

    @Test
    void testUpdateDespesaNotFound() {
        when(despesaService.update(anyLong(), any(Despesa.class))).thenThrow(EntityNotFoundException.class);

        ResponseEntity<Despesa> response = despesaController.update(1L, despesa);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteDespesa() {
        doNothing().when(despesaService).delete(anyLong());

        ResponseEntity<Void> response = despesaController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteDespesaNotFound() {
        doThrow(EntityNotFoundException.class).when(despesaService).delete(anyLong());

        ResponseEntity<Void> response = despesaController.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindAll() {
        List<Despesa> despesaList = Collections.singletonList(despesa);
        when(despesaService.findAll()).thenReturn(despesaList);

        ResponseEntity<List<Despesa>> response = despesaController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(despesaList, response.getBody());
    }

    @Test
    void testFindById() {
        when(despesaService.findById(anyLong())).thenReturn(Optional.of(despesa));

        ResponseEntity<Despesa> response = despesaController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(despesa, response.getBody());
    }

    @Test
    void testFindByStatus() {
        when(despesaService.findByStatus(any(StatusPagamento.class))).thenReturn(Collections.singletonList(despesa));

        ResponseEntity<List<Despesa>> response = despesaController.findByStatus(StatusPagamento.PAGO);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(Collections.singletonList(despesa), response.getBody());
    }

    @Test
    void testFindByStatusNotFound() {
        when(despesaService.findByStatus(any(StatusPagamento.class))).thenThrow(new RuntimeException());

        ResponseEntity<List<Despesa>> response = despesaController.findByStatus(StatusPagamento.PAGO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindByPagamentoPendente() {
        when(despesaService.findByPagamentoPendente()).thenReturn(Collections.singletonList(despesa));

        ResponseEntity<List<Despesa>> response = despesaController.findByPagamentoPendente();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(Collections.singletonList(despesa), response.getBody());
    }

    @Test
    void testFindByPagamentoPendenteNotFound() {
        when(despesaService.findByPagamentoPendente()).thenThrow(new RuntimeException());

        ResponseEntity<List<Despesa>> response = despesaController.findByPagamentoPendente();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindByVencimento() {
        when(despesaService.findByVencimento(any(StatusPagamento.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(despesa));

        ResponseEntity<List<Despesa>> response = despesaController.findByVencimento(StatusPagamento.PAGO, LocalDate.now());

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(Collections.singletonList(despesa), response.getBody());
    }

    @Test
    void testFindByVencimentoNotFound() {
        when(despesaService.findByVencimento(any(StatusPagamento.class), any(LocalDate.class)))
                .thenThrow(new RuntimeException());

        ResponseEntity<List<Despesa>> response = despesaController.findByVencimento(StatusPagamento.PAGO, LocalDate.now());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    void testFindCategorias() {
        List<String> categorias = Arrays.asList("Alimentação", "Saúde", "Transporte");

        when(despesaService.findCategorias()).thenReturn(categorias);

        ResponseEntity<List<String>> response = despesaController.findCategorias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categorias, response.getBody());
    }

}
