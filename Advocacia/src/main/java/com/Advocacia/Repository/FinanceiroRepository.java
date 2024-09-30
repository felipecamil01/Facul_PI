package com.Advocacia.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.StatusPagamento;


@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro,Long> {

    @Query("SELECT f FROM Financeiro f WHERE f.statusPagamento = :statusPagamento")
    List<Financeiro> findAllByStatusPagamento(@Param("statusPagamento") StatusPagamento statusPagamento);

    @Query("SELECT f FROM Financeiro f WHERE f.statusPagamento = :statusPagamento AND f.dataVencimentoParcelas = :dataVencimentoParcelas")
    List<Financeiro> findByVencimento(@Param("statusPagamento") StatusPagamento statusPagamento, @Param("dataVencimentoParcelas") LocalDate dataVencimentoParcelas);

}
