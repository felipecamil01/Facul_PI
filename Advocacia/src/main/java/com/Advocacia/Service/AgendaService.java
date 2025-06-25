package com.Advocacia.Service;

import com.Advocacia.DTO.AgendaDTO;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Agenda;
import com.Advocacia.Entity.Status;
import com.Advocacia.Repository.ClienteRepository;
import com.Advocacia.Repository.AgendaRepository;
import com.Advocacia.Util.ContatoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AgendaRepository agendaRepository;

    public Agenda save(AgendaDTO agendaDTO) {
        Cliente cliente = clienteRepository.findById(agendaDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + agendaDTO.getClienteId()));
        Agenda agenda = ContatoMapper.toEntity(agendaDTO, cliente);
        agenda.setStatus(Status.ATIVO);
        return agendaRepository.save(agenda);
    }

    public Agenda update(Long id, AgendaDTO contatoDto) {
        Agenda contatoExistente = agendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado com ID: " + id));

        Cliente cliente = clienteRepository.findById(contatoDto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + contatoDto.getClienteId()));
        contatoExistente.setDataUltimoContato(contatoDto.getDataUltimoContato());
        contatoExistente.setMeioContato(contatoDto.getMeioContato());
        contatoExistente.setNotasContato(contatoDto.getNotasContato());
        contatoExistente.setProximoPassos(contatoDto.getProximoPassos());
        contatoExistente.setCliente(cliente);
        contatoExistente.setStatus(Status.ATIVO);
        return agendaRepository.save(contatoExistente);
    }
    
    public void delete(Long id) {
        Optional<Agenda> contato =  this.agendaRepository.findById(id);
        if (contato.isPresent()) {
            Agenda contato1 = contato.get();
            contato1.setStatus(Status.INATIVO);
            agendaRepository.save(contato1);
        } else
            throw new EntityNotFoundException("Contato não encontrado");
    }

    public List<Agenda> findAll() {

        return agendaRepository.findAllAtivos(Status.ATIVO);
    }

    public Optional<Agenda> findById(Long id) {

        return agendaRepository.findById(id);
    }


    public List<Agenda> findByIdcliente(Long clienteId) {
        return agendaRepository.findByClienteId(clienteId);
    }
}
