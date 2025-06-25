package com.Advocacia.DTO;

import java.time.LocalDate;

public record AgendaAudDTO(
    long id,
    LocalDate dataUltimoContato,
    String meioContato,
    String notasContato,
    String proximoPassos,
    String status,
    long clienteId,
    String clienteNome,
    String usuario,
    long timestamp,
    String tipoModificacao
    
) {}
