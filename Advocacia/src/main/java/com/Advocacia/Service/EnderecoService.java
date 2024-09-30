package com.Advocacia.Service;

import com.Advocacia.Entity.Endereco;
import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;


    public Endereco save(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public Endereco update(Long id, Endereco enderecoAtualizado) {
        Optional<Endereco> enderecoExistente = enderecoRepository.findById(id);

        if (enderecoExistente.isPresent()) {
            enderecoAtualizado.setId(id);
            return enderecoRepository.save(enderecoAtualizado);
        }

        throw new EntityNotFoundException("Endereço não encontrado");
    }

    public void delete(Long id) {
        if (enderecoRepository.existsById(id)) {
            enderecoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Endereço não encontrado");
        }
    }

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> findById(Long id) {
        return enderecoRepository.findById(id);
    }
    
}
