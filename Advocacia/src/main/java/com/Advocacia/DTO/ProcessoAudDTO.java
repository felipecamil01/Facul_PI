package com.Advocacia.DTO;

import java.time.LocalDate;
import java.util.List;

public record ProcessoAudDTO(
    long id,
    String tipoCliente,
    String areaAtuacao,
    String numeroProcesso,
    String comarca,
    LocalDate dataInicio,
    String descricao,
    String andamento,
    String situacaoAtual,
    String status,
    List<LocalDate> prazosImportantes,
    long clienteId,
    String clienteNome,
    String usuario,
    long timestamp,
    String tipoModificacao
) {}
