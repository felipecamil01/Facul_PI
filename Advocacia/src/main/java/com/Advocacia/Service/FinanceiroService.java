package com.Advocacia.Service;

import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Repository.FinanceiroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceiroService {
    @Autowired
    private FinanceiroRepository financeiroRepository;

    public Financeiro salvarFinanceiro(Financeiro financeiro) {

        return financeiroRepository.save(financeiro);
    }

    public Page<Financeiro> listarTodosFinanceiros(Pageable pageable) {
        return financeiroRepository.findAll(pageable);
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

    public List<Financeiro>buscarPorStatus(StatusPagamento statusPagamento){
        return financeiroRepository.findAllByStatusPagamento(statusPagamento);
    }

    public List<Financeiro>buscarPagamentoPendente(){
        return financeiroRepository.findAllByStatusPagamento(StatusPagamento.PENDENTE);
    }

    public List<Financeiro>buscarVencimentos(StatusPagamento statusPagamento, LocalDateTime data){
        return financeiroRepository.findAllByStatusPagamentoAndDataVencimentoParcelas(statusPagamento, data);
    }
}
