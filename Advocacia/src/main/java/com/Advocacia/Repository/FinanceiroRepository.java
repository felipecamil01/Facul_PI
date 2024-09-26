package com.Advocacia.Repository;

import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro,Long> {

    List<Financeiro> findAllByStatusPagamento(StatusPagamento statusPagamento);
    List<Financeiro> findAllByStatusPagamentoAndDataVencimentoParcelas(StatusPagamento statusPagamento, LocalDateTime dataVencimentoParcelas);
}
