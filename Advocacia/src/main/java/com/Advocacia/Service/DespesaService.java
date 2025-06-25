package com.Advocacia.Service;

import com.Advocacia.Entity.Despesa;
import com.Advocacia.Entity.Status;
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
    private DespesaRepository despesaRepository;

    public Despesa save(Despesa financeiro) {
    	financeiro.setStatus(Status.ATIVO);
        return despesaRepository.save(financeiro);
    }

    public Despesa update(Long id, Despesa financeiroAtualizado) {
        Optional<Despesa> financeiroExistente = despesaRepository.findById(id);

        if (financeiroExistente.isPresent()) {
            financeiroAtualizado.setId(id);
            financeiroAtualizado.setStatus(Status.ATIVO);
            return despesaRepository.save(financeiroAtualizado);
        }

        throw new EntityNotFoundException("Financeiro não encontrado");
    }

    public void delete(Long id) {
        Optional<Despesa> despesa =  this.despesaRepository.findById(id);
        if (despesa.isPresent()) {
        	Despesa despesa1 = despesa.get();
        	despesa1.setStatus(Status.INATIVO);
        	despesaRepository.save(despesa1);
        } else
            throw new EntityNotFoundException("Contato não encontrado");
    }

    public List<Despesa> findAll() {
        return despesaRepository.findAllAtivos(Status.ATIVO);
    }

    public Optional<Despesa> findById(Long id) {
        return despesaRepository.findById(id);
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
