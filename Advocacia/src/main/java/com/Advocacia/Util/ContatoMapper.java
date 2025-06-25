package com.Advocacia.Util;

import com.Advocacia.DTO.AgendaDTO;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Agenda;

public class ContatoMapper {
    public static Agenda toEntity(AgendaDTO dto, Cliente cliente) {
        Agenda contato = new Agenda();
        contato.setDataUltimoContato(dto.getDataUltimoContato());
        contato.setMeioContato(dto.getMeioContato());
        contato.setNotasContato(dto.getNotasContato());
        contato.setProximoPassos(dto.getProximoPassos());
        contato.setCliente(cliente);
        return contato;
    }

    public static AgendaDTO toDto(Agenda contato) {
        AgendaDTO dto = new AgendaDTO();
        dto.setId(contato.getId());
        dto.setDataUltimoContato(contato.getDataUltimoContato());
        dto.setMeioContato(contato.getMeioContato());
        dto.setNotasContato(contato.getNotasContato());
        dto.setProximoPassos(contato.getProximoPassos());
        dto.setClienteId(contato.getCliente().getId());
        return dto;
    }


}
