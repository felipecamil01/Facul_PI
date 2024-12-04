package com.Advocacia.ControllerTest;

import com.Advocacia.Controller.ClienteController;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Service.ClienteService;
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
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Pedro");

        when(clienteService.save(any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.save(cliente);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void testUpdateCliente() {
        Long id = 1L;
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("Jane Doe");

        when(clienteService.update(eq(id), any(Cliente.class))).thenReturn(clienteAtualizado);

        ResponseEntity<Cliente> response = clienteController.update(id, clienteAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteAtualizado, response.getBody());
    }

    @Test
    void testUpdateClienteNotFound() {
        Long id = 1L;
        Cliente clienteAtualizado = new Cliente();

        when(clienteService.update(eq(id), any(Cliente.class))).thenThrow(new EntityNotFoundException());

        ResponseEntity<Cliente> response = clienteController.update(id, clienteAtualizado);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteCliente() {
        Long id = 1L;

        doNothing().when(clienteService).delete(id);

        ResponseEntity<Void> response = clienteController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteClienteNotFound() {
        Long id = 1L;

        doThrow(new EntityNotFoundException()).when(clienteService).delete(id);

        ResponseEntity<Void> response = clienteController.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindAll() {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente());

        when(clienteService.findAll()).thenReturn(clientes);

        ResponseEntity<List<Cliente>> response = clienteController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientes, response.getBody());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome("Pedro");

        when(clienteService.findById(id)).thenReturn(Optional.of(cliente));

        ResponseEntity<Cliente> response = clienteController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void testFindByNome() {
        String nome = "Pedro";
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente());

        when(clienteService.findByNome(nome)).thenReturn(clientes);

        ResponseEntity<List<Cliente>> response = clienteController.findByNome(nome);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientes, response.getBody());
    }
}
