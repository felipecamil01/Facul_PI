package com.Advocacia.Service;

import com.Advocacia.DTO.ContatoDto;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Contato;
import com.Advocacia.Repository.ClienteRepository;
import com.Advocacia.Repository.ContatoRepository;
import com.Advocacia.Util.ContatoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContatoService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ContatoRepository contatoRepository;

    public Contato save(ContatoDto contatoDto) {
        Cliente cliente = clienteRepository.findById(contatoDto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + contatoDto.getClienteId()));
        Contato contato = ContatoMapper.toEntity(contatoDto, cliente);

        return contatoRepository.save(contato);
    }

    public Contato update(Long id, ContatoDto contatoDto) {
        Contato contatoExistente = contatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado com ID: " + id));

        Cliente cliente = clienteRepository.findById(contatoDto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + contatoDto.getClienteId()));
        contatoExistente.setDataUltimoContato(contatoDto.getDataUltimoContato());
        contatoExistente.setMeioContato(contatoDto.getMeioContato());
        contatoExistente.setNotasContato(contatoDto.getNotasContato());
        contatoExistente.setProximoPassos(contatoDto.getProximoPassos());
        contatoExistente.setCliente(cliente);

        return contatoRepository.save(contatoExistente);
    }

    public void delete(Long id) {
        if (contatoRepository.existsById(id)) {
            contatoRepository.deleteById(id);
        } else
            throw new EntityNotFoundException("Contato não encontrado");
    }

    public List<Contato> findAll() {

        return contatoRepository.findAll();
    }

    public Optional<Contato> findById(Long id) {

        return contatoRepository.findById(id);
    }


    public List<Contato> findByIdcliente(Long clienteId) {
        return contatoRepository.findByClienteId(clienteId);
    }
}
