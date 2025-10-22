package com.Advocacia.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import com.Advocacia.DTO.PrazoImportanteDTO;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoResumoDTO {
  private Long id;
  private String numeroProcesso;
  private String areaAtuacao;
  private String situacaoAtual;
  private LocalDate dataInicio;
    private List<PrazoImportanteDTO> prazosImportantes;
  private List<DocumentoResumoDTO> documentos;
}

