package com.Advocacia.Service;

import com.Advocacia.Entity.Contato;
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

    public Contato salvarContato(Contato contato) {
        return contatoRepository.save(contato);
    }

    public Page<Contato> listarTodosContatos(Pageable pageable) {


        return contatoRepository.findAll(pageable);
    }

    public Optional<Contato> buscarContatoPorId(Long id) {
        return contatoRepository.findById(id);
    }

    public Contato atualizarContato(Long id, Contato contatoAtualizado) {
        Optional<Contato> contatoExistente = contatoRepository.findById(id);

        if (contatoExistente.isPresent()) {
            contatoAtualizado.setId(id);
            return contatoRepository.save(contatoAtualizado);
        }

        throw new EntityNotFoundException("Contato não encontrado");
    }

    public void deletarContato(Long id) {
        if (contatoRepository.existsById(id)) {
            contatoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Contato não encontrado");
        }
    }
}
