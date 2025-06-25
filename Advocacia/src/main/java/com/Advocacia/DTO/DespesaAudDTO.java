package com.Advocacia.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaAudDTO(
    long id,
    BigDecimal honorario,
    BigDecimal despesasAdicionais,
    LocalDate dataVencimento,
    String categoriaDespesa,
    String formaPagamento,
    String statusPagamento,
    String observacao,
    String status,
    long clienteId,
    String clienteNome,
    String usuario,
    long timestamp,
    String tipoModificacao
) {}
