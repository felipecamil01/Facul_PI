package com.Advocacia.Service;

import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.StatusCliente;
import com.Advocacia.Repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
	
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente save(Cliente cliente) {
        cliente.setStatusCliente(StatusCliente.ATIVO);
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            clienteAtualizado.setStatusCliente(StatusCliente.ATIVO);
            clienteAtualizado.setId(id);
            return clienteRepository.save(clienteAtualizado);
        }else
        	throw new EntityNotFoundException("Cliente não encontrado");
    }

    public void delete(Long id) {
        Optional<Cliente> cliente =  this.clienteRepository.findById(id);
        if (cliente.isPresent()) {
            Cliente cliente1 = cliente.get();
            cliente1.setStatusCliente(StatusCliente.INATIVO);
            clienteRepository.save(cliente1);
        } else 
            throw new EntityNotFoundException("Cliente não encontrado");
    }
    
    public List<Cliente> findAll() {

        return clienteRepository.findAllAtivos(StatusCliente.ATIVO);
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }
    
    public List<Cliente> findByNome(String nome) {

        return clienteRepository.findByNome(nome);
    }

}
