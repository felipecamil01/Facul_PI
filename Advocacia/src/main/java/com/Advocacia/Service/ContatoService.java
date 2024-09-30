package com.Advocacia.Service;

import com.Advocacia.Entity.Contato;
import com.Advocacia.Entity.Documento;
import com.Advocacia.Repository.ContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContatoService {
	
    @Autowired
    private ContatoRepository contatoRepository;

    public Contato save(Contato contato) {

        return contatoRepository.save(contato);
    }

    public Contato update(Long id, Contato contatoAtualizado) {
        if (contatoRepository.existsById(id)) {
            contatoAtualizado.setId(id);
            return contatoRepository.save(contatoAtualizado);
        } else
        	throw new EntityNotFoundException("Contato não encontrado");
    }

    public void delete(Long id) {
        if (contatoRepository.existsById(id)) {
            contatoRepository.deleteById(id);
        } else 
            throw new EntityNotFoundException("Contato não encontrado");
    }

    public List<Contato> findAll() {

        return contatoRepository.findAll();
    }

    public Optional<Contato> findById(Long id) {

        return contatoRepository.findById(id);
    }
}
