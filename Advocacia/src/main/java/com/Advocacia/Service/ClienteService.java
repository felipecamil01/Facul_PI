package com.Advocacia.Service;

import com.Advocacia.DTO.ClienteDTO;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Enum.StatusCliente;
import com.Advocacia.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente save(Cliente cliente) {
        cliente.setStatusCliente(StatusCliente.ATIVO);
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente clienteAtualizado) {
        clienteRepository.findById(id).orElseThrow();
        clienteAtualizado.setStatusCliente(StatusCliente.ATIVO);
        clienteAtualizado.setId(id);
        return clienteRepository.save(clienteAtualizado);
    }

    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow();
        cliente.setStatusCliente(StatusCliente.INATIVO);
        clienteRepository.save(cliente);
    }

    public List<ClienteDTO> findAll() {

        List<Cliente> clientes = clienteRepository.findAll(); // isso retorna List<Cliente>
        var clientesdto = clientes.stream()
                .map(ClienteDTO::new) // aqui o construtor recebe Cliente, então tá ok
                .collect(Collectors.toList());
        return clientesdto;

    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow();
    }

    public List<Cliente> findByNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
}