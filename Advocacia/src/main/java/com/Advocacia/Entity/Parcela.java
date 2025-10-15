package com.Advocacia.Entity;

import com.Advocacia.Enum.StatusPagamento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Audited
public class Parcela {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pagamento_id", nullable = false)
  @JsonBackReference
  private Pagamento pagamento;

  private int numeroParcela;

  @Column(nullable = false)
  private BigDecimal valorParcela;

  @Column(nullable = false)
  private LocalDate dataVencimento;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusPagamento statusPagamento; // PENDENTE, PAGO, ATRASADO


}
