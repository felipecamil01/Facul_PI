package com.Advocacia.Entity;

import jakarta.persistence.*;
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
    
    private String tipoCliente;
    
    private String areaAtuacao;
    
    private String numeroProcesso;
    
    private String comarca;
    
    @PastOrPresent
    private LocalDate dataInicio;
    
    private String descricao;
    
    private  String andamento;
    
    private String situacaoAtual;
    
    @ElementCollection
    private List<LocalDate>prazosImportantes;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToMany
    @JoinTable(name = "processo_documento",
            joinColumns = @JoinColumn(name="processo_id"),
            inverseJoinColumns = @JoinColumn(name = "documento_id"))
    private List<Documento> documentos=new ArrayList<>();

}
