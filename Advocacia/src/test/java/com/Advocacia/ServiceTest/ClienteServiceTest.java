package com.Advocacia.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Endereco;
import com.Advocacia.Entity.StatusCivil;
import com.Advocacia.Entity.StatusCliente;
import com.Advocacia.Repository.ClienteRepository;
import com.Advocacia.Service.ClienteService;
import jakarta.persistence.EntityNotFoundException;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Endereco endereco;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setLogradouro("Rua Castelão");
        endereco.setNumero("384");
        endereco.setComplemento("Casa");
        endereco.setBairro("Morumbi");
        endereco.setCidade("Foz do Iguaçu");
        endereco.setUf("PR");
        endereco.setCep("85858-440");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Pedro Henrique Alves dos Santos");
        cliente.setEmail("pedrohalvesdosantos@gmail.com");
        cliente.setCpf("121.753.469-51");
        cliente.setRg("10.677.359-9");
        cliente.setProfissao("Estudante");
        cliente.setTelefone("(45)99851-7717");
        cliente.setDataNascimento(LocalDate.of(2004, 10, 22));
        cliente.setEstadoCivil(StatusCivil.SOLTEIRO);
        cliente.setEndereco(endereco);
        cliente.setProcessos(null);

    }

    @Test
    void testSaveCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente savedCliente = clienteService.save(cliente);

        assertNotNull(savedCliente);
        assertEquals(StatusCliente.ATIVO, savedCliente.getStatusCliente());

        verify(clienteRepository, times(1)).save(cliente);
    }
    
    @Test
    void testUpdateCliente() {
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("Pedro Henrique Alves dos Santos Atualizado");
        clienteAtualizado.setEmail("novoemail@gmail.com");
        clienteAtualizado.setCpf("121.753.469-51");
        clienteAtualizado.setRg("10.677.359-9");
        clienteAtualizado.setProfissao("Estudante");
        clienteAtualizado.setTelefone("(45)99851-7717");
        clienteAtualizado.setDataNascimento(LocalDate.of(2004, 10, 22));
        clienteAtualizado.setEstadoCivil(StatusCivil.SOLTEIRO);
        clienteAtualizado.setEndereco(endereco);
        
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente updatedCliente = clienteService.update(1L, clienteAtualizado);

        assertNotNull(updatedCliente);
        assertEquals(cliente.getId(), updatedCliente.getId());
        assertEquals(clienteAtualizado.getNome(), updatedCliente.getNome());
        assertEquals(clienteAtualizado.getEmail(), updatedCliente.getEmail());
        assertEquals(clienteAtualizado.getCpf(), updatedCliente.getCpf());
        assertEquals(clienteAtualizado.getRg(), updatedCliente.getRg());
        assertEquals(clienteAtualizado.getProfissao(), updatedCliente.getProfissao());
        assertEquals(clienteAtualizado.getTelefone(), updatedCliente.getTelefone());
        assertEquals(clienteAtualizado.getDataNascimento(), updatedCliente.getDataNascimento());
        assertEquals(clienteAtualizado.getEstadoCivil(), updatedCliente.getEstadoCivil());
        assertEquals(clienteAtualizado.getEndereco(), updatedCliente.getEndereco());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
    
    @Test
    void testUpdateClienteNotFound() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clienteService.update(2L, new Cliente());
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
    }
    
    @Test
    void testDeleteCliente() {
    	when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.delete(1L);

        assertEquals(StatusCliente.INATIVO, cliente.getStatusCliente());
        verify(clienteRepository, times(1)).save(cliente);
    }
    
    @Test
    public void testDeleteClienteNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clienteService.delete(1L);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testFindAll() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("João Silva");
        cliente1.setStatusCliente(StatusCliente.ATIVO);

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Maria Oliveira");
        cliente2.setStatusCliente(StatusCliente.ATIVO);

        List<Cliente> clientesAtivos = List.of(cliente1, cliente2);

        when(clienteRepository.findAllAtivos(StatusCliente.ATIVO)).thenReturn(clientesAtivos);

        List<Cliente> foundClientesAtivos = clienteService.findAll();

        assertNotNull(foundClientesAtivos);
        assertEquals(2, foundClientesAtivos.size());
        assertTrue(foundClientesAtivos.stream().allMatch(c -> c.getStatusCliente() == StatusCliente.ATIVO));
        verify(clienteRepository, times(1)).findAllAtivos(StatusCliente.ATIVO);
    }

    @Test
    void testFindById() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

        Optional<Cliente> foundCliente = clienteService.findById(1L);

        assertTrue(foundCliente.isPresent());
        assertEquals(1L, foundCliente.get().getId());
        verify(clienteRepository, times(1)).findById(1L);
    }
    
    @Test
    void testFindByNome() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("João Silva");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Maria Oliveira");

        when(clienteRepository.findByNome("joao")).thenReturn(List.of(cliente1));

        List<Cliente> foundClientes = clienteService.findByNome("joao");

        assertNotNull(foundClientes);
        assertEquals(1, foundClientes.size());
        assertEquals("João Silva", foundClientes.get(0).getNome());
        verify(clienteRepository, times(1)).findByNome("joao");
    }
}
