package com.Advocacia.Entity;

import com.Advocacia.Enum.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.math.RoundingMode;
import jakarta.persistence.Transient;

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

  private LocalDate dataPagamento; // Data base para quitação/primeiro vencimento

  @PrePersist
  protected void onCreate() {
    dataCriacao = LocalDate.now();
  }

  @Transient
  private BigDecimal valorAParcelar;

  @Transient
  private BigDecimal valorParcelaCalculada;

  @JsonProperty("valorAParcelar")
  public BigDecimal getValorAParcelar() {
    BigDecimal entradaEfetiva = entrada != null ? entrada : BigDecimal.ZERO;
    BigDecimal total = valorTotal != null ? valorTotal : BigDecimal.ZERO;
    return total.subtract(entradaEfetiva);
  }

  @JsonProperty("valorParcelaCalculada")
  public BigDecimal getValorParcelaCalculada() {
    BigDecimal aParcelar = getValorAParcelar();
    if (tipoPagamento == TipoPagamento.A_VISTA) {
      return aParcelar;
    }
    int n = (numeroParcelas != null && numeroParcelas > 0) ? numeroParcelas : 1;
    return aParcelar.divide(new BigDecimal(n), 2, RoundingMode.HALF_UP);
  }

  // Getters e Setters
}
