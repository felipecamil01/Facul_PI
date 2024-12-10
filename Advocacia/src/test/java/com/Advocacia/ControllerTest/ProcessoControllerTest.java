package com.Advocacia.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.Advocacia.Controller.ProcessoController;
import com.Advocacia.Entity.Processo;
import com.Advocacia.Service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;

class ProcessoControllerTest {

    @Mock
    private ProcessoService processoService;

    @InjectMocks
    private ProcessoController processoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MockMvcBuilders.standaloneSetup(processoController).build();
    }

    @Test
    void testSaveProcesso() throws Exception {
        Processo processo = new Processo();
        processo.setTipoCliente("Cliente A");
        processo.setNumeroProcesso("12345");
        processo.setDataInicio(LocalDate.now());
        
        when(processoService.save(any(Processo.class))).thenReturn(processo);

        ResponseEntity<Processo> response = processoController.save(processo);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(processo, response.getBody());
        verify(processoService).save(any(Processo.class));
    }

    @Test
    void testUpdateProcesso() throws Exception {
        Long id = 1L;
        Processo processoAtualizado = new Processo();
        processoAtualizado.setTipoCliente("Cliente B");

        when(processoService.update(eq(id), any(Processo.class))).thenReturn(processoAtualizado);

        ResponseEntity<Processo> response = processoController.update(id, processoAtualizado);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(processoAtualizado, response.getBody());
        verify(processoService).update(eq(id), any(Processo.class));
    }
    
    @Test
    void testUpdateProcessoNotFound() throws Exception {
        Long id = 1L;
        Processo processoAtualizado = new Processo();
        processoAtualizado.setTipoCliente("Cliente B");

        doThrow(new EntityNotFoundException()).when(processoService).update(eq(id), any(Processo.class));

        ResponseEntity<Processo> response = processoController.update(id, processoAtualizado);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(processoService).update(eq(id), any(Processo.class));
    }

    @Test
    void testDeleteProcesso() throws Exception {
        Long id = 1L;

        doNothing().when(processoService).delete(id);

        ResponseEntity<Void> response = processoController.delete(id);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(processoService).delete(id);
    }

    @Test
    void testDeleteProcessoNotFound() throws Exception {
        Long id = 1L;

        doThrow(new EntityNotFoundException()).when(processoService).delete(id);

        ResponseEntity<Void> response = processoController.delete(id);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(processoService).delete(id);
    }

    @Test
    void testFindAll() throws Exception {
        Processo processo1 = new Processo();
        processo1.setTipoCliente("Cliente A");
        Processo processo2 = new Processo();
        processo2.setTipoCliente("Cliente B");

        List<Processo> processoList = Arrays.asList(processo1, processo2);
        when(processoService.findAll()).thenReturn(processoList);

        ResponseEntity<List<Processo>> response = processoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(processoService).findAll();
    }


    @Test
    void testFindById() throws Exception {
        Long id = 1L;
        Processo processo = new Processo();
        processo.setId(id);

        when(processoService.findById(id)).thenReturn(Optional.of(processo));

        ResponseEntity<Processo> response = processoController.findById(id);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(processo, response.getBody());
        verify(processoService).findById(id);
    }

    @Test
    void testFindByNumeroProcesso() throws Exception {
        String numero = "12345";
        Processo processo = new Processo();
        processo.setNumeroProcesso(numero);
        
        when(processoService.findByNumeroProcesso(numero)).thenReturn(Arrays.asList(processo));

        ResponseEntity<List<Processo>> response = processoController.findByNumeroProcesso(numero);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(numero, response.getBody().get(0).getNumeroProcesso());
        verify(processoService).findByNumeroProcesso(numero);
    }
}
