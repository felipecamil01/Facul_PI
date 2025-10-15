package com.Advocacia.Repository;

import com.Advocacia.Entity.Parcela;
import com.Advocacia.Enum.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
  // Este método customizado é essencial para encontrar todas as parcelas
  // com um status específico (ex: "PENDENTE") para a verificação de atrasos.
  List<Parcela> findByStatusPagamento(StatusPagamento statusPagamento);
}
