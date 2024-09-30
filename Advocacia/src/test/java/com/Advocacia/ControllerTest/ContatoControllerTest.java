package com.Advocacia.ControllerTest;

import com.Advocacia.Controller.ContatoController;
import com.Advocacia.Entity.Contato;
import com.Advocacia.Service.ContatoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ContatoControllerTest {

    @InjectMocks
    private ContatoController contatoController;

    @Mock
    private ContatoService contatoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveContato() {
        Contato contato = new Contato();
        contato.setId(1L);

        when(contatoService.save(any(Contato.class))).thenReturn(contato);

        ResponseEntity<Contato> response = contatoController.save(contato);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(contato, response.getBody());
    }

    @Test
    void testUpdateContato() {
        Long id = 1L;
        Contato contatoAtualizado = new Contato();

        when(contatoService.update(eq(id), any(Contato.class))).thenReturn(contatoAtualizado);

        ResponseEntity<Contato> response = contatoController.update(id, contatoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contatoAtualizado, response.getBody());
    }

    @Test
    void testUpdateContatoNotFound() {
        Long id = 1L;
        Contato contatoAtualizado = new Contato();

        when(contatoService.update(eq(id), any(Contato.class))).thenThrow(new EntityNotFoundException());

        ResponseEntity<Contato> response = contatoController.update(id, contatoAtualizado);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteContato() {
        Long id = 1L;

        doNothing().when(contatoService).delete(id);

        ResponseEntity<Void> response = contatoController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteContatoNotFound() {
        Long id = 1L;

        doThrow(new EntityNotFoundException()).when(contatoService).delete(id);

        ResponseEntity<Void> response = contatoController.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindAll() {
        List<Contato> contatoList = new ArrayList<>();
        contatoList.add(new Contato());

        when(contatoService.findAll()).thenReturn(contatoList);

        ResponseEntity<List<Contato>> response = contatoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contatoList, response.getBody());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Contato contato = new Contato();
        contato.setId(id);

        when(contatoService.findById(id)).thenReturn(Optional.of(contato));

        ResponseEntity<Contato> response = contatoController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contato, response.getBody());
    }

}
