package com.Advocacia.Service;

import com.Advocacia.Entity.Despesa;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DespesaService {
	
    @Autowired
    private DespesaRepository despesaRepository;

    public Despesa save(Despesa financeiro) {
        return despesaRepository.save(financeiro);
    }

    public Despesa update(Long id, Despesa financeiroAtualizado) {
    	despesaRepository.findById(id).orElseThrow();
        financeiroAtualizado.setId(id);
        return despesaRepository.save(financeiroAtualizado);
    }

    public void delete(Long id) {
    	Despesa despesa = despesaRepository.findById(id).orElseThrow();
    	despesaRepository.delete(despesa);
    }

    public List<Despesa> findAll() {
        return despesaRepository.findAll();
    }

    public Despesa findById(Long id) {
        return despesaRepository.findById(id).orElseThrow();
    }

    public List<Despesa>findByStatus(StatusPagamento statusPagamento){
        return despesaRepository.findAllByStatusPagamento(statusPagamento);
    }

    public List<Despesa>findByPagamentoPendente(){
        return despesaRepository.findAllByStatusPagamento(StatusPagamento.PENDENTE);
    }

    public List<Despesa>findByVencimento(StatusPagamento statusPagamento, LocalDate data){
        return despesaRepository.findByVencimento(statusPagamento, data);
    }
    
    public List<String> findCategorias(){
        return despesaRepository.findCategorias();
    }
}