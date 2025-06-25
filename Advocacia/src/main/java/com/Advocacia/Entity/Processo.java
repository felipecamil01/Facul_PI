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

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
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
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ElementCollection
    private List<LocalDate>prazosImportantes;
    
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToMany
    @JoinTable(name = "processo_documento",
            joinColumns = @JoinColumn(name="processo_id"),
            inverseJoinColumns = @JoinColumn(name = "documento_id"))
    private List<Documento> documentos=new ArrayList<>();

}
