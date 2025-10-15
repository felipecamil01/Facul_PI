package com.Advocacia.Entity;

import com.Advocacia.Auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
public class Processo extends AuditoriaEntity<String> {

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

  private String andamento;

  private String situacaoAtual;

  @ElementCollection
  private List<LocalDate> prazosImportantes;

  @ManyToOne
  @JoinColumn(name = "cliente_id")
  private Cliente cliente;

  @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Agenda> agendas = new ArrayList<>();

  @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Documento> documentos = new ArrayList<>();


  public void setDocumentos(List<Documento> documentos) {
    if (documentos != null) {
      this.documentos.clear();
      for (Documento documento : documentos) {
        this.addDocumento(documento);
      }
    }
  }

  public void addDocumento(Documento documento) {
    this.documentos.add(documento);
    documento.setProcesso(this);
  }

}
