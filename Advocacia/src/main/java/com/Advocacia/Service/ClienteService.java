package com.Advocacia.Service;

import com.Advocacia.Entity.Cliente;
import com.Advocacia.Repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;


    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }


    public Page<Cliente> listarClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }


    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }


    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isPresent()) {
            clienteAtualizado.setId(id);
            return clienteRepository.save(clienteAtualizado);
        }

        throw new EntityNotFoundException("Cliente não encontrado");
    }


    public void deletarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
    }


    public List<Cliente> buscarClientesPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
}
