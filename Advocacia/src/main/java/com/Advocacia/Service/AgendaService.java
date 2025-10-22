package com.Advocacia.Service;

import com.Advocacia.Entity.Agenda;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Parcela;
import com.Advocacia.Entity.Processo;
import com.Advocacia.Repository.AgendaRepository;
import com.Advocacia.Repository.ParcelaRepository;
import com.Advocacia.Repository.ProcessoRepository;
import com.Advocacia.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgendaService {
    
    @Autowired
    private AgendaRepository contatoRepository;

    @Autowired
  private ProcessoRepository processoRepository;

  @Autowired
  private ParcelaRepository parcelaRepository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Transactional
  public Agenda save(Agenda agendaNova) {
    prepararRelacionamentos(agendaNova);
    return contatoRepository.save(agendaNova);
  }

    @Transactional
    public Agenda update(Long id, Agenda agendaAtualiazada) {
        contatoRepository.findById(id).orElseThrow();
        agendaAtualiazada.setId(id);
        prepararRelacionamentos(agendaAtualiazada);
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

  private void prepararRelacionamentos(Agenda agenda) {
    Processo processo = null;
    if (agenda.getProcesso() != null && agenda.getProcesso().getId() != 0) {
      processo = processoRepository.findById(agenda.getProcesso().getId())
        .orElseThrow(() -> new IllegalArgumentException("Processo informado não existe."));
      agenda.setProcesso(processo);
    }

    if (agenda.getCliente() != null && agenda.getCliente().getId() != 0) {
      Cliente cliente = clienteRepository.findById(agenda.getCliente().getId())
        .orElseThrow(() -> new IllegalArgumentException("Cliente informado não existe."));
      agenda.setCliente(cliente);
    }

    if (agenda.getParcela() != null && agenda.getParcela().getId() != null) {
      Parcela parcela = parcelaRepository.findById(agenda.getParcela().getId())
        .orElseThrow(() -> new IllegalArgumentException("Parcela informada não existe."));
      agenda.setParcela(parcela);
      if (parcela.getPagamento() != null && parcela.getPagamento().getCliente() != null) {
                agenda.setCliente(parcela.getPagamento().getCliente());
            }
        }

    if (agenda.getCliente() == null && processo != null) {
      Cliente clienteProcesso = processo.getCliente();
      if (clienteProcesso == null) {
        throw new IllegalArgumentException("Processo informado precisa estar vinculado a um cliente.");
      }
      agenda.setCliente(clienteProcesso);
    }

    boolean marcadoComoPrazo = agenda.isPrazoImportante() ||
      (agenda.getTipo() != null && agenda.getTipo().equalsIgnoreCase("PRAZO_IMPORTANTE"));
    agenda.setPrazoImportante(marcadoComoPrazo);
  }
}
