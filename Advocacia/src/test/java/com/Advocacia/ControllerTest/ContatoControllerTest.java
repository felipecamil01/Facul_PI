package com.Advocacia.ControllerTest;

import com.Advocacia.Controller.AgendaController;
import com.Advocacia.Entity.Agenda;
import com.Advocacia.Service.AgendaService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ContatoControllerTest {

	/*
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
        ContatoDto contatoDto = new ContatoDto();
        contatoDto.setId(1L);

        Contato contato = new Contato();
        contato.setId(1L);

        when(contatoService.save(contatoDto)).thenReturn(contato);

        ResponseEntity<Contato> response = contatoController.save(contatoDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(contato, response.getBody());
    }
    
    @Test
    void testSaveContatoNotFound() {
        ContatoDto contatoDto = new ContatoDto();
        contatoDto.setId(1L);

        when(contatoService.save(contatoDto)).thenThrow(new EntityNotFoundException("Contato não encontrado"));

        ResponseEntity<Contato> response = contatoController.save(contatoDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateContato() {
        Long id = 1L;
        ContatoDto contatoDto = new ContatoDto();
        Contato contatoAtualizado = new Contato();

        when(contatoService.update(eq(id), eq(contatoDto))).thenReturn(contatoAtualizado);

        ResponseEntity<Contato> response = contatoController.update(id, contatoDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contatoAtualizado, response.getBody());
    }

    @Test
    void testUpdateContatoNotFound() {
        Long id = 1L;
        ContatoDto contatoDto = new ContatoDto();

        when(contatoService.update(eq(id), eq(contatoDto))).thenThrow(new EntityNotFoundException());

        ResponseEntity<Contato> response = contatoController.update(id, contatoDto);

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

    @Test
    void testFindByIdCliente() {
        Long clienteId = 1L;
        List<Contato> contatos = new ArrayList<>();
        contatos.add(new Contato());

        when(contatoService.findByIdcliente(clienteId)).thenReturn(contatos);

        ResponseEntity<List<Contato>> response = contatoController.findByIdCliente(clienteId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contatos, response.getBody());
    }
    */
}
