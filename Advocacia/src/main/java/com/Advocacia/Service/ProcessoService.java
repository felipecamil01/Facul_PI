package com.Advocacia.Service;

import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Processo;
import com.Advocacia.Entity.StatusCliente;
import com.Advocacia.Repository.ProcessoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    public Processo save(Processo processo) {
        return processoRepository.save(processo);
    }

    public Processo update(Long id, Processo processoAtualizado) {
        Optional<Processo> processoExistente = processoRepository.findById(id);

        if (processoExistente.isPresent()) {
            processoAtualizado.setId(id);
            return processoRepository.save(processoAtualizado);
        }

        throw new EntityNotFoundException("Processo não encontrado");
    }

    public void delete(Long id) {
        if (processoRepository.existsById(id)) {
            processoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Processo não encontrado");
        }
    }

    public List<Processo> findAll() {
        return processoRepository.findAll();
    }

    public Optional<Processo> findById(Long id) {
        return processoRepository.findById(id);
    }

    public List<Processo> findByNumeroProcesso(String numero) {
        return processoRepository.findByNumeroProcesso(numero);
    }
    
}
