package com.Advocacia.DTO;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter

public class HistoricoDTO {
  private int revisao;
  private String usuario;
  private Instant data;
  private Object dados;

  public HistoricoDTO(int revisao, String usuario, Instant data, Object dados) {
    this.revisao = revisao;
    this.usuario = usuario;
    this.data = data;
    this.dados = dados;
  }
}
