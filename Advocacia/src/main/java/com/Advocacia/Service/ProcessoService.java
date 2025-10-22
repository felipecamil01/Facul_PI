package com.Advocacia.Service;

import com.Advocacia.Entity.Processo;
import com.Advocacia.Entity.Agenda;
import com.Advocacia.Entity.PrazoImportante;
import com.Advocacia.Repository.ProcessoRepository;
import com.Advocacia.Repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    public Processo save(Processo processo) {
        validarCliente(processo);
        Processo salvo = processoRepository.save(processo);
        sincronizarPrazosComAgenda(salvo);
        return salvo;
    }

    public Processo update(Long id, Processo processoAtualizado) {
    	processoRepository.findById(id).orElseThrow();
    	processoAtualizado.setId(id);
        validarCliente(processoAtualizado);
        Processo salvo = processoRepository.save(processoAtualizado);
        sincronizarPrazosComAgenda(salvo);
        return salvo;
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

    public List<Processo> findByCliente(Long clienteId) {
        return processoRepository.findByClienteId(clienteId);
    }

    private void validarCliente(Processo processo) {
        if (processo.getCliente() == null || processo.getCliente().getId() == 0) {
            throw new IllegalArgumentException("Processo precisa estar vinculado a um cliente antes de ser salvo.");
        }
    }

    private void sincronizarPrazosComAgenda(Processo processo) {
        if (processo.getId() == 0) {
            return;
        }

        List<Agenda> existentes = agendaRepository.findByProcessoId(processo.getId());
        for (Agenda agenda : existentes) {
            if (agenda.isPrazoImportante()) {
                agendaRepository.delete(agenda);
            }
        }

        if (processo.getPrazosImportantes() == null) {
            return;
        }

        List<Agenda> novas = new ArrayList<>();
        for (PrazoImportante prazo : processo.getPrazosImportantes()) {
            if (prazo == null) continue;
            LocalDateTime data = prazo.getData();
            if (data == null) {
                data = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
            }
            Agenda agenda = new Agenda();
            agenda.setProcesso(processo);
            agenda.setCliente(processo.getCliente());
            agenda.setDescricao(prazo.getDescricao() != null && !prazo.getDescricao().isBlank()
                ? prazo.getDescricao()
                : "Prazo importante - Processo " + processo.getNumeroProcesso());
            agenda.setTipo("PRAZO_IMPORTANTE");
            agenda.setPrazoImportante(true);
            agenda.setPrioridade("ALTA");
            agenda.setData(data);
            novas.add(agenda);
        }
        if (!novas.isEmpty()) {
            agendaRepository.saveAll(novas);
        }
    }
}


