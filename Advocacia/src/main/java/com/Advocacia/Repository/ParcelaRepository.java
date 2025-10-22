package com.Advocacia.Repository;

import com.Advocacia.Entity.Parcela;
import com.Advocacia.Enum.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

  /**
   * Recupera todas as parcelas com o status informado.
   */
  List<Parcela> findByStatusPagamento(StatusPagamento statusPagamento);

  /**
   * Busca parcelas dentro do intervalo informado aplicando filtros opcionais de cliente e status.
   */
  @Query("""
      SELECT parc
      FROM Parcela parc
      JOIN FETCH parc.pagamento pag
      JOIN FETCH pag.cliente cli
      WHERE parc.dataVencimento BETWEEN :dataInicio AND :dataFim
        AND (:status IS NULL OR parc.statusPagamento = :status)
        AND (:clienteId IS NULL OR cli.id = :clienteId)
      ORDER BY cli.nome ASC, parc.dataVencimento ASC, parc.numeroParcela ASC
      """)
  List<Parcela> buscarParcelasParaRelatorio(@Param("dataInicio") LocalDate dataInicio,
                                            @Param("dataFim") LocalDate dataFim,
                                            @Param("status") StatusPagamento status,
                                            @Param("clienteId") Long clienteId);

  List<Parcela> findByPagamentoId(Long pagamentoId);
}
