package com.Advocacia.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import com.Advocacia.Controller.EnderecoController;
import com.Advocacia.Entity.Endereco;
import com.Advocacia.Service.EnderecoService;
import jakarta.persistence.EntityNotFoundException;

class EnderecoControllerTest {

    @InjectMocks
    private EnderecoController enderecoController;

    @Mock
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEndereco() {
        Endereco endereco = new Endereco();
        when(enderecoService.save(any(Endereco.class))).thenReturn(endereco);

        ResponseEntity<Endereco> response = enderecoController.save(endereco);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(endereco, response.getBody());
        verify(enderecoService, times(1)).save(endereco);
    }

    @Test
    void testUpdateEndereco() {
        Long id = 1L;
        Endereco enderecoAtualizado = new Endereco();
        when(enderecoService.update(eq(id), any(Endereco.class))).thenReturn(enderecoAtualizado);

        ResponseEntity<Endereco> response = enderecoController.update(id, enderecoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enderecoAtualizado, response.getBody());
        verify(enderecoService, times(1)).update(eq(id), any(Endereco.class));
    }

    @Test
    void testUpdateEnderecoNotFound() {
        Long id = 1L;
        Endereco enderecoAtualizado = new Endereco();
        when(enderecoService.update(eq(id), any(Endereco.class))).thenThrow(new EntityNotFoundException());

        ResponseEntity<Endereco> response = enderecoController.update(id, enderecoAtualizado);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteEndereco() {
        Long id = 1L;
        doNothing().when(enderecoService).delete(id);

        ResponseEntity<Void> response = enderecoController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(enderecoService, times(1)).delete(id);
    }

    @Test
    void testDeleteEnderecoNotFound() {
        Long id = 1L;
        doThrow(new EntityNotFoundException()).when(enderecoService).delete(id);

        ResponseEntity<Void> response = enderecoController.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindAll() {
        Endereco endereco1 = new Endereco();
        Endereco endereco2 = new Endereco();
        List<Endereco> enderecoList = Arrays.asList(endereco1, endereco2);

        when(enderecoService.findAll()).thenReturn(enderecoList);

        ResponseEntity<List<Endereco>> response = enderecoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }



    @Test
    void testFindById() {
        Long id = 1L;
        Endereco endereco = new Endereco();
        when(enderecoService.findById(id)).thenReturn(Optional.of(endereco));

        ResponseEntity<Endereco> response = enderecoController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(endereco, response.getBody());
    }

}
