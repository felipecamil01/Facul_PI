package com.Advocacia.DTO;

import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Enum.TipoPagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioMensalItemDTO {
  private Long pagamentoId;
  private Long parcelaId;
  private String clienteNome;
  private Integer numeroParcela;
  private LocalDate dataVencimento;
  private StatusPagamento statusPagamento;
  private BigDecimal valorParcela;
  private TipoPagamento tipoPagamento;
}
