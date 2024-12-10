package com.Advocacia.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class ContatoDto {
        private Long id;
        private LocalDate dataUltimoContato;
        private String meioContato;
        private String notasContato;
        private String proximoPassos;
        private Long clienteId;
    }
