package com.Advocacia.ServiceTest;

import com.Advocacia.DTO.ContatoDto;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Contato;
import com.Advocacia.Repository.ClienteRepository;
import com.Advocacia.Repository.ContatoRepository;
import com.Advocacia.Service.ContatoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class ContatoServiceTest {

    @InjectMocks
    private ContatoService contatoService;

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    private Contato contato;
    private ContatoDto contatoDto;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);

        contato = new Contato();
        contato.setId(1L);
        contato.setDataUltimoContato(LocalDate.now());
        contato.setMeioContato("Telefone");
        contato.setNotasContato("Nota de teste");
        contato.setProximoPassos("Agendar reunião");
        contato.setCliente(cliente);

        contatoDto = new ContatoDto();
        contatoDto.setClienteId(1L);
        contatoDto.setDataUltimoContato(LocalDate.now());
        contatoDto.setMeioContato("Email");
        contatoDto.setNotasContato("Outra nota");
        contatoDto.setProximoPassos("Enviar proposta");
    }

    @Test
    void testSave() {
        Mockito.when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        Mockito.when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato savedContato = contatoService.save(contatoDto);

        assertNotNull(savedContato);
        assertEquals(1L, savedContato.getId());
        assertEquals(cliente, savedContato.getCliente());
    }

    @Test
    void testSaveClienteNotFound() {
        Mockito.when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> contatoService.save(contatoDto));

        assertEquals("Cliente não encontrado com ID: 1", exception.getMessage());
    }

    @Test
    void testUpdate() {
        Mockito.when(contatoRepository.findById(anyLong())).thenReturn(Optional.of(contato));
        Mockito.when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        Mockito.when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato updatedContato = contatoService.update(1L, contatoDto);

        assertNotNull(updatedContato);
        assertEquals("Email", updatedContato.getMeioContato());
        assertEquals("Enviar proposta", updatedContato.getProximoPassos());
    }

    @Test
    void testUpdateContatoNotFound() {
        Mockito.when(contatoRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> contatoService.update(1L, contatoDto));

        assertEquals("Contato não encontrado com ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateClienteNotFound() {
        Mockito.when(contatoRepository.findById(anyLong())).thenReturn(Optional.of(contato));
        Mockito.when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> contatoService.update(1L, contatoDto));

        assertEquals("Cliente não encontrado com ID: 1", exception.getMessage());
    }

    @Test
    void testDelete() {
        Mockito.when(contatoRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> contatoService.delete(1L));

        Mockito.verify(contatoRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        Mockito.when(contatoRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> contatoService.delete(1L));

        assertEquals("Contato não encontrado", exception.getMessage());
    }

    @Test
    void testFindAll() {
        List<Contato> contatos = Collections.singletonList(contato);
        Mockito.when(contatoRepository.findAll()).thenReturn(contatos);

        List<Contato> result = contatoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Mockito.when(contatoRepository.findById(anyLong())).thenReturn(Optional.of(contato));

        Optional<Contato> foundContato = contatoService.findById(1L);

        assertTrue(foundContato.isPresent());
        assertEquals(1L, foundContato.get().getId());
    }

    @Test
    void testFindByIdCliente() {
        List<Contato> contatos = Collections.singletonList(contato);
        Mockito.when(contatoRepository.findByClienteId(anyLong())).thenReturn(contatos);

        List<Contato> result = contatoService.findByIdcliente(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
