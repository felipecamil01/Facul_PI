package com.Advocacia.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.Advocacia.Entity.Despesa;
import com.Advocacia.Enum.StatusPagamento;


@Repository
public interface DespesaRepository extends JpaRepository<Despesa,Long> {

    @Query("SELECT f FROM Despesa f WHERE f.statusPagamento = :statusPagamento")
    List<Despesa> findAllByStatusPagamento(@Param("statusPagamento") StatusPagamento statusPagamento);

    @Query("SELECT f FROM Despesa f WHERE f.statusPagamento = :statusPagamento AND f.dataVencimento = :dataVencimento")
    List<Despesa> findByVencimento(@Param("statusPagamento") StatusPagamento statusPagamento, @Param("dataVencimento") LocalDate dataVencimento);

    @Query("SELECT d.categoriaDespesa " +
            "FROM Despesa d " +
            "GROUP BY d.categoriaDespesa " +
            "ORDER BY COUNT(d) DESC")
    List<String> findCategorias();
}
