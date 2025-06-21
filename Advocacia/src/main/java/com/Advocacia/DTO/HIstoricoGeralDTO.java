package com.Advocacia.DTO;

import java.time.Instant;

public class HIstoricoGeralDTO {
  private String entidade;
  private Long entidadeId;
  private int revisao;
  private String usuario;
  private Instant data;
  private Object dados;

  public HIstoricoGeralDTO(String entidade, Long entidadeId, int revisao, String usuario, Instant data, Object dados) {
    this.entidade = entidade;
    this.entidadeId = entidadeId;
    this.revisao = revisao;
    this.usuario = usuario;
    this.data = data;
    this.dados = dados;
  }
}
