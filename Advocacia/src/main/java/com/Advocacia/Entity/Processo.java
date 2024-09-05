package com.Advocacia.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Processo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String tipoCliente;

    @NotBlank
    private String areaAtuacao;

    @NotBlank
    private String numeroProcesso;

    @NotBlank
    private String comarca;

    @PastOrPresent
    private LocalDate dataInicio;

    @NotBlank
    private String descricao;

    @NotBlank
    private  String andamento;

    @NotBlank
    private String situacaoAtual;// em andamento, finalizado ou arquivado

    @ElementCollection
    private List<LocalDate>prazosImportantes;


    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToMany
    @JoinTable(name = "processo_documento",
            joinColumns = @JoinColumn(name="processo_id"),
            inverseJoinColumns = @JoinColumn(name = "documento_id"))
    private List<Documentos> documentos=new ArrayList<>();


}
