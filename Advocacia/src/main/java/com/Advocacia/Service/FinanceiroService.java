package com.Advocacia.Service;

import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.Processo;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Repository.FinanceiroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceiroService {
	
    @Autowired
    private FinanceiroRepository financeiroRepository;

    public Financeiro save(Financeiro financeiro) {
        return financeiroRepository.save(financeiro);
    }

    public Financeiro update(Long id, Financeiro financeiroAtualizado) {
        Optional<Financeiro> financeiroExistente = financeiroRepository.findById(id);

        if (financeiroExistente.isPresent()) {
            financeiroAtualizado.setId(id);
            return financeiroRepository.save(financeiroAtualizado);
        }

        throw new EntityNotFoundException("Financeiro não encontrado");
    }

    public void delete(Long id) {
        if (financeiroRepository.existsById(id)) {
            financeiroRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Financeiro não encontrado");
        }
    }

    public List<Financeiro> findAll() {
        return financeiroRepository.findAll();
    }

    public Optional<Financeiro> findById(Long id) {
        return financeiroRepository.findById(id);
    }

    public List<Financeiro>findByStatus(StatusPagamento statusPagamento){
        return financeiroRepository.findAllByStatusPagamento(statusPagamento);
    }

    public List<Financeiro>findByPagamentoPendente(){
        return financeiroRepository.findAllByStatusPagamento(StatusPagamento.PENDENTE);
    }

    public List<Financeiro>findByVencimento(StatusPagamento statusPagamento, LocalDate data){
        return financeiroRepository.findByVencimento(statusPagamento, data);
    }
}
