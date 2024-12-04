package com.Advocacia.ServiceTest;

import com.Advocacia.Entity.Endereco;
import com.Advocacia.Repository.EnderecoRepository;
import com.Advocacia.Service.EnderecoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setLogradouro("Rua A");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 45");
        endereco.setBairro("Centro");
        endereco.setCidade("Cidade XYZ");
        endereco.setUf("SP");
        endereco.setCep("12345-678");
    }

    @Test
    void testSaveEndereco() {
        Mockito.when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco savedEndereco = enderecoService.save(endereco);

        assertNotNull(savedEndereco);
        assertEquals(1L, savedEndereco.getId());
        assertEquals("Rua A", savedEndereco.getLogradouro());
    }

    @Test
    void testUpdateEndereco() {
        Mockito.when(enderecoRepository.findById(anyLong())).thenReturn(Optional.of(endereco));
        Mockito.when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco updatedEndereco = enderecoService.update(1L, endereco);

        assertNotNull(updatedEndereco);
        assertEquals(1L, updatedEndereco.getId());
    }

    @Test
    void testUpdateEnderecoNotFound() {
        Mockito.when(enderecoRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            enderecoService.update(1L, endereco);
        });

        assertEquals("Endereço não encontrado", exception.getMessage());
    }

    @Test
    void testDeleteEndereco() {
        Mockito.when(enderecoRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> enderecoService.delete(1L));

        Mockito.verify(enderecoRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEnderecoNotFound() {
        Mockito.when(enderecoRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            enderecoService.delete(1L);
        });

        assertEquals("Endereço não encontrado", exception.getMessage());
    }

    @Test
    void testFindAll() {
        List<Endereco> enderecoList = Collections.singletonList(endereco);
        Mockito.when(enderecoRepository.findAll()).thenReturn(enderecoList);

        List<Endereco> result = enderecoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Mockito.when(enderecoRepository.findById(anyLong())).thenReturn(Optional.of(endereco));

        Optional<Endereco> foundEndereco = enderecoService.findById(1L);

        assertTrue(foundEndereco.isPresent());
        assertEquals(1L, foundEndereco.get().getId());
    }
}
