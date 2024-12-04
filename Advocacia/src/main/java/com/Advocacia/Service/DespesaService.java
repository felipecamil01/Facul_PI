package com.Advocacia.Service;

import com.Advocacia.Entity.Despesa;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Repository.DespesaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {
	
    @Autowired
    private DespesaRepository financeiroRepository;

    public Despesa save(Despesa financeiro) {
        return financeiroRepository.save(financeiro);
    }

    public Despesa update(Long id, Despesa financeiroAtualizado) {
        Optional<Despesa> financeiroExistente = financeiroRepository.findById(id);

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

    public List<Despesa> findAll() {
        return financeiroRepository.findAll();
    }

    public Optional<Despesa> findById(Long id) {
        return financeiroRepository.findById(id);
    }

    public List<Despesa>findByStatus(StatusPagamento statusPagamento){
        return financeiroRepository.findAllByStatusPagamento(statusPagamento);
    }

    public List<Despesa>findByPagamentoPendente(){
        return financeiroRepository.findAllByStatusPagamento(StatusPagamento.PENDENTE);
    }

    public List<Despesa>findByVencimento(StatusPagamento statusPagamento, LocalDate data){
        return financeiroRepository.findByVencimento(statusPagamento, data);
    }
    
    public List<String> findCategorias(){
        return financeiroRepository.findCategorias();

    }
}
