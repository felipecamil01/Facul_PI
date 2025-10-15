package com.Advocacia.Repository;

import com.Advocacia.Entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

  /**
   * Encontra todos os pagamentos associados a um ID de cliente específico.
   * Usado para o relatório individual do cliente.
   */
  List<Pagamento> findByClienteId(Long clienteId);

  /**
   * Encontra todos os pagamentos cuja data de criação está entre duas datas.
   * Este é o método principal que usaremos para os relatórios mensais e anuais.
   */
  List<Pagamento> findByDataCriacaoBetween(LocalDate dataInicio, LocalDate dataFim);

}
