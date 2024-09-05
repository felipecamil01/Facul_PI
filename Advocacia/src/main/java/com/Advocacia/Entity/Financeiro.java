package com.Advocacia.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private BigDecimal honorado;

    @NotBlank
    private String formaPagamento;

    @NotBlank
    private String statusPagamento;

    @FutureOrPresent
    private LocalDate dataVencimentoParcelas;

    @ElementCollection
    private List<LocalDate>historicoPagamentos;

    private BigDecimal despesasAdicionais;

}
