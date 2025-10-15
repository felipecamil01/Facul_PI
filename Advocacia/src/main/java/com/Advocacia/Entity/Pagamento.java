package com.Advocacia.Entity;

import com.Advocacia.Enum.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Audited
public class Pagamento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoPagamento tipoPagamento; // Ex: A_VISTA, PARCELADO

  @Column(nullable = false)
  private BigDecimal valorTotal;

  private BigDecimal entrada; // Valor pago no ato

  private Integer numeroParcelas; // De 1 a 15

  @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Parcela> parcelas;

  private String observacao;

  private LocalDate dataCriacao;

  @PrePersist
  protected void onCreate() {
    dataCriacao = LocalDate.now();
  }

  // Getters e Setters
}
