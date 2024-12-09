package com.Advocacia.UtilTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.Advocacia.DTO.ContatoDto;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Contato;
import com.Advocacia.Util.ContatoMapper;

public class ContatoMapperTest {
	
	@Test
	void testMapperClassInstantiations() {
	    new ContatoMapper();
	}

	
	@Test
    void testToEntity() {
        ContatoDto dto = new ContatoDto();
        dto.setDataUltimoContato(LocalDate.now());
        dto.setMeioContato("Telefone");
        dto.setNotasContato("Contato realizado com sucesso");
        dto.setProximoPassos("Aguardar resposta");
        
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Contato contato = ContatoMapper.toEntity(dto, cliente);

        assertNotNull(contato);
        assertEquals(dto.getDataUltimoContato(), contato.getDataUltimoContato());
        assertEquals(dto.getMeioContato(), contato.getMeioContato());
        assertEquals(dto.getNotasContato(), contato.getNotasContato());
        assertEquals(dto.getProximoPassos(), contato.getProximoPassos());
        assertEquals(cliente, contato.getCliente());
    }

    @Test
    void testToDto() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        
        Contato contato = new Contato();
        contato.setId(1L);
        contato.setDataUltimoContato(LocalDate.now());
        contato.setMeioContato("E-mail");
        contato.setNotasContato("Notas do contato");
        contato.setProximoPassos("Seguir com o processo");
        contato.setCliente(cliente);

        ContatoDto dto = ContatoMapper.toDto(contato);

        assertNotNull(dto);
        assertEquals(contato.getId(), dto.getId());
        assertEquals(contato.getDataUltimoContato(), dto.getDataUltimoContato());
        assertEquals(contato.getMeioContato(), dto.getMeioContato());
        assertEquals(contato.getNotasContato(), dto.getNotasContato());
        assertEquals(contato.getProximoPassos(), dto.getProximoPassos());
        assertEquals(contato.getCliente().getId(), dto.getClienteId());
    }
}
