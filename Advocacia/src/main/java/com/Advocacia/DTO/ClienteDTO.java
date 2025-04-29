package com.Advocacia.DTO;

import com.Advocacia.Entity.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class ClienteDTO {
  private long id;
  private String nome;
  private String email;
  private String cpf;
  private String rg;
  private String profissao;
  private String telefone;
  private LocalDate dataNascimento;
  private StatusCivil estadoCivil;
  private StatusCliente statusCliente;
  private Endereco endereco;

  // Construtor que recebe um Cliente
  public ClienteDTO(Cliente cliente) {
    this.id = cliente.getId();
    this.nome = cliente.getNome();
    this.email = cliente.getEmail();
    this.cpf = mascararCpf(cliente.getCpf());
    this.rg = mascararRG(cliente.getRg());
    this.profissao = cliente.getProfissao();
    this.telefone = cliente.getTelefone();
    this.dataNascimento = cliente.getDataNascimento();
    this.estadoCivil = cliente.getEstadoCivil();
    this.statusCliente = cliente.getStatusCliente();
    this.endereco = cliente.getEndereco();
  }

  private String mascararRG(String rg) {
    if (rg == null || rg.length() < 9) {
      return rg;
    }

    // Remove pontuação se existir
    String digits = rg.replaceAll("\\D", "");

    if (digits.length() < 9) {
      return rg; // formato inválido
    }


    String parte1 = digits.substring(2, 5);
    String parte2 = digits.substring(5, 7);
    return "**." + parte1 + "." + parte2 + "*-*";
  }
  private String mascararCpf(String cpf) {
    if (cpf == null || cpf.length() != 11) {
    var parte1=cpf.substring(4,7);
    var parte2=cpf.substring(8,12);
      return "***."+ parte1 + "." + parte2 +"**";
    }
  return cpf;

  }


}
