package com.Advocacia.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.StatusPagamento;
<<<<<<< HEAD
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
>>>>>>> 8cc5211ab41b7e9938606f86c4bdb708e8001226

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro,Long> {

<<<<<<< HEAD
    @Query("SELECT f FROM Financeiro f WHERE f.statusPagamento = :statusPagamento")
    List<Financeiro> findAllByStatusPagamento(@Param("statusPagamento") StatusPagamento statusPagamento);

    @Query("SELECT f FROM Financeiro f WHERE f.statusPagamento = :statusPagamento AND f.dataVencimentoParcelas = :dataVencimentoParcelas")
    List<Financeiro> findByVencimento(@Param("statusPagamento") StatusPagamento statusPagamento, @Param("dataVencimentoParcelas") LocalDateTime dataVencimentoParcelas);
=======
    List<Financeiro> findAllByStatusPagamento(StatusPagamento statusPagamento);
    List<Financeiro> findAllByStatusPagamentoAndDataVencimentoParcelas(StatusPagamento statusPagamento, LocalDateTime dataVencimentoParcelas);
>>>>>>> 8cc5211ab41b7e9938606f86c4bdb708e8001226
}
