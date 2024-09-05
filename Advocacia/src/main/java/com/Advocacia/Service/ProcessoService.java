package com.Advocacia.Service;

import com.Advocacia.Entity.Processo;
import com.Advocacia.Repository.ProcessoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcessoService {
    @Autowired
    private ProcessoRepository processoRepository;

    public Processo salvarProcesso(Processo processo) {
        return processoRepository.save(processo);
    }

    public List<Processo> listarTodosProcessos() {
        return processoRepository.findAll();
    }

    public Optional<Processo> buscarProcessoPorId(Long id) {
        return processoRepository.findById(id);
    }

    public Processo atualizarProcesso(Long id, Processo processoAtualizado) {
        Optional<Processo> processoExistente = processoRepository.findById(id);

        if (processoExistente.isPresent()) {
            processoAtualizado.setId(id);
            return processoRepository.save(processoAtualizado);
        }

        throw new EntityNotFoundException("Processo não encontrado");
    }

    public void deletarProcesso(Long id) {
        if (processoRepository.existsById(id)) {
            processoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Processo não encontrado");
        }
    }


}
