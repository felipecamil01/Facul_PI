package com.Advocacia.Service;

import com.Advocacia.Entity.Processo;
import com.Advocacia.Repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    public Processo save(Processo processo) {
        return processoRepository.save(processo);
    }

    public Processo update(Long id, Processo processoAtualizado) {
    	processoRepository.findById(id).orElseThrow();
    	processoAtualizado.setId(id);
        return processoRepository.save(processoAtualizado);
    }

    public void delete(Long id) {
    	Processo processo = processoRepository.findById(id).orElseThrow();
    	processoRepository.delete(processo);
    }

    public List<Processo> findAll() {
        return processoRepository.findAll();
    }

    public Processo findById(Long id) {
        return processoRepository.findById(id).orElseThrow();
    }

    public List<Processo> findByNumeroProcesso(String numero) {
        return processoRepository.findByNumeroProcesso(numero);
    }   
}