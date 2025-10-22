package com.Advocacia.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoResumoDTO {
  private Long id;
  private String titulo;
  private LocalDate dataRecebimento;
  private String statusDocumento;
  private String observacao;
  private String processoNumero;
  private Long processoId;
  private String caminhoArquivo;
}
