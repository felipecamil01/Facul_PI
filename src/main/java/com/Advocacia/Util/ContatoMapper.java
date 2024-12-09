package com.Advocacia.Util;

import com.Advocacia.DTO.ContatoDto;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Contato;

public class ContatoMapper {
    public static Contato toEntity(ContatoDto dto, Cliente cliente) {
        Contato contato = new Contato();
        contato.setDataUltimoContato(dto.getDataUltimoContato());
        contato.setMeioContato(dto.getMeioContato());
        contato.setNotasContato(dto.getNotasContato());
        contato.setProximoPassos(dto.getProximoPassos());
        contato.setCliente(cliente);
        return contato;
    }

    public static ContatoDto toDto(Contato contato) {
        ContatoDto dto = new ContatoDto();
        dto.setId(contato.getId());
        dto.setDataUltimoContato(contato.getDataUltimoContato());
        dto.setMeioContato(contato.getMeioContato());
        dto.setNotasContato(contato.getNotasContato());
        dto.setProximoPassos(contato.getProximoPassos());
        dto.setClienteId(contato.getCliente().getId());
        return dto;
    }


}
