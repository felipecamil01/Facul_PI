package com.Advocacia.Service;

import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Repository.FinanceiroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinanceiroService {
    @Autowired
    private FinanceiroRepository financeiroRepository;

    public Financeiro salvarFinanceiro(Financeiro financeiro) {
        return financeiroRepository.save(financeiro);
    }

    public List<Financeiro> listarTodosFinanceiros() {
        return financeiroRepository.findAll();
    }

    public Optional<Financeiro> buscarFinanceiroPorId(Long id) {
        return financeiroRepository.findById(id);
    }

    public Financeiro atualizarFinanceiro(Long id, Financeiro financeiroAtualizado) {
        Optional<Financeiro> financeiroExistente = financeiroRepository.findById(id);

        if (financeiroExistente.isPresent()) {
            financeiroAtualizado.setId(id);
            return financeiroRepository.save(financeiroAtualizado);
        }

        throw new EntityNotFoundException("Financeiro não encontrado");
    }

    public void deletarFinanceiro(Long id) {
        if (financeiroRepository.existsById(id)) {
            financeiroRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Financeiro não encontrado");
        }
    }

}
