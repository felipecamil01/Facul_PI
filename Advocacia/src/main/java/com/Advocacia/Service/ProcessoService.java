package com.Advocacia.Service;

import com.Advocacia.Entity.Processo;
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

    // Salvar um novo processo
    public Processo salvarProcesso(Processo processo) {
        return processoRepository.save(processo);
    }

    // Listar todos os processos com paginação
    public Page<Processo> listarProcessos(Pageable pageable) {
        return processoRepository.findAll(pageable);
    }

    // Buscar processo por ID
    public Optional<Processo> buscarProcessoPorId(Long id) {
        return processoRepository.findById(id);
    }

    // Buscar processos por número do processo
    public List<Processo> buscarProcessosPorNumero(String numero) {
        return processoRepository.findByNumeroProcesso(numero);
    }

    // Atualizar processo existente
    public Processo atualizarProcesso(Long id, Processo processoAtualizado) {
        Optional<Processo> processoExistente = processoRepository.findById(id);

        if (processoExistente.isPresent()) {
            processoAtualizado.setId(id);
            return processoRepository.save(processoAtualizado);
        }

        throw new EntityNotFoundException("Processo não encontrado");
    }

    // Deletar processo por ID
    public void deletarProcesso(Long id) {
        if (processoRepository.existsById(id)) {
            processoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Processo não encontrado");
        }
    }
}
