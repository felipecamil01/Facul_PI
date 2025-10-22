package com.Advocacia.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaResumoDTO {
  private Long id;
  private String descricao;
  private String tipo;
  private boolean prazoImportante;
  private String prioridade;
  private LocalDateTime data;
  private Long processoId;
  private String processoNumero;
}
