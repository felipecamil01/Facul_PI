package com.Advocacia.DTO;

import com.Advocacia.Enum.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioMensalDTO {
  private int ano;
  private int mes;
  private Long clienteId;
  private String clienteNome;
  private StatusPagamento filtroStatus;
  private BigDecimal totalGeral;
  private BigDecimal totalPago;
  private BigDecimal totalAtrasado;
  private BigDecimal totalPendente;
  @Builder.Default
  private List<RelatorioMensalItemDTO> itens = new ArrayList<>();
}
