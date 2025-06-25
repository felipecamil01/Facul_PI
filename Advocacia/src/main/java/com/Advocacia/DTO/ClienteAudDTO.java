package com.Advocacia.DTO;

import java.time.LocalDate;

import com.Advocacia.Entity.Endereco;

public record ClienteAudDTO(
	    long id,
	    String nome,
	    String email,
	    String cpf,
	    String rg,
	    String profissao,
	    String telefone,
	    LocalDate dataNascimento,
	    String estadoCivil,
	    String status,
	    Endereco endereco,
	    String usuario,
	    long timestamp,
	    String tipoModificacao
	) {}

