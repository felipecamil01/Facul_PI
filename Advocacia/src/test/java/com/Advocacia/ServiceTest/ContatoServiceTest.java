package com.Advocacia.ServiceTest;

import com.Advocacia.Entity.Contato;
import com.Advocacia.Repository.ContatoRepository;
import com.Advocacia.Service.ContatoService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class ContatoServiceTest {

    @InjectMocks
    private ContatoService contatoService;

    @Mock
    private ContatoRepository contatoRepository;

    private Contato contato;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        contato = new Contato();
        contato.setId(1L);
        contato.setDataUltimoContato(LocalDate.now());
        contato.setMeioContato("meio contato");
        contato.setNotasContato("notas contato");
        contato.setProximoPassos("proximo passos");
    }

    @Test
    void testsSaveContato() {
        Mockito.when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato savedContato = contatoService.save(contato);

        assertNotNull(savedContato);
        assertEquals(1L, savedContato.getId());
    }

    @Test
    void testUpdateContato() {
        Mockito.when(contatoRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato updatedContato = contatoService.update(1L, contato);

        assertNotNull(updatedContato);
        assertEquals(1L, updatedContato.getId());
    }

    @Test
    void testUpdateNotFound() {
        Mockito.when(contatoRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contatoService.update(1L, contato);
        });

        assertEquals("Contato não encontrado", exception.getMessage());
    }

    @Test
    void testDeleteContato() {
        Mockito.when(contatoRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> contatoService.delete(1L));

        Mockito.verify(contatoRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteContatoNotFound() {
        Mockito.when(contatoRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contatoService.delete(1L);
        });

        assertEquals("Contato não encontrado", exception.getMessage());
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Contato> contatoPage = new PageImpl<>(Collections.singletonList(contato));
        Mockito.when(contatoRepository.findAll(any(Pageable.class))).thenReturn(contatoPage);

        Page<Contato> result = contatoService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindById() {
        Mockito.when(contatoRepository.findById(anyLong())).thenReturn(Optional.of(contato));

        Optional<Contato> foundContato = contatoService.findById(1L);

        assertTrue(foundContato.isPresent());
        assertEquals(1L, foundContato.get().getId());
    }
}
