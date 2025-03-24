package com.Advocacia.Service;

import com.Advocacia.Entity.Agenda;
import com.Advocacia.Repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgendaService {
    
    @Autowired
    private AgendaRepository contatoRepository;

    public Agenda save(Agenda agendaNova) {
        return contatoRepository.save(agendaNova);
    }

    public Agenda update(Long id, Agenda agendaAtualiazada) {
        contatoRepository.findById(id).orElseThrow();
        agendaAtualiazada.setId(id);
        return contatoRepository.save(agendaAtualiazada);
    }

    public void delete(Long id) {
        Agenda contato = contatoRepository.findById(id).orElseThrow();
        contatoRepository.delete(contato);
    }

    public List<Agenda> findAll() {
        return contatoRepository.findAll();
    }

    public Agenda findById(Long id) {
        return contatoRepository.findById(id).orElseThrow();
    }
}