package com.Advocacia.Service;

import com.Advocacia.Entity.Endereco;
import com.Advocacia.Repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco save(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public Endereco update(Long id, Endereco enderecoAtualizado) {
    	enderecoRepository.findById(id).orElseThrow();
    	enderecoAtualizado.setId(id);
        return enderecoRepository.save(enderecoAtualizado);
    }

    public void delete(Long id) {
    	Endereco endereco = enderecoRepository.findById(id).orElseThrow();
    	enderecoRepository.delete(endereco);
    }

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Endereco findById(Long id) {
        return enderecoRepository.findById(id).orElseThrow();
    }
}