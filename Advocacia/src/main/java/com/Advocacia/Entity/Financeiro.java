package com.Advocacia.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private BigDecimal honorado;
    
    private String formaPagamento;
    
    private StatusPagamento statusPagamento;
    
    @FutureOrPresent
    private LocalDate dataVencimentoParcelas;
    
    @ElementCollection
    private List<LocalDate>historicoPagamentos;
    
    private BigDecimal despesasAdicionais;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
