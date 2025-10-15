package com.Advocacia.Service;

import com.Advocacia.Entity.Pagamento;
import com.Advocacia.Entity.Parcela;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Enum.TipoPagamento;
import com.Advocacia.Repository.PagamentoRepository;
import com.Advocacia.Repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PagamentoService {

  @Autowired
  private PagamentoRepository pagamentoRepository;
  @Autowired
  private ParcelaRepository parcelaRepository;

  @Transactional
  public Pagamento createPagamento(Pagamento pagamento) {
    if (pagamento.getTipoPagamento() == TipoPagamento.PARCELADO) {
      gerarParcelas(pagamento);
    } else {
      Parcela unicaParcela = new Parcela();
      unicaParcela.setPagamento(pagamento);
      unicaParcela.setNumeroParcela(1);
      unicaParcela.setValorParcela(pagamento.getValorTotal());
      unicaParcela.setDataVencimento(LocalDate.now().plusDays(15));
      unicaParcela.setStatusPagamento(StatusPagamento.PENDENTE);
      pagamento.setParcelas(List.of(unicaParcela));
    }
    return pagamentoRepository.save(pagamento);
  }

  public List<Pagamento> findAll() {
    return pagamentoRepository.findAll();
  }

  public Pagamento findById(Long id) {
    return pagamentoRepository.findById(id)
      .orElseThrow(() -> new NoSuchElementException("Pagamento não encontrado com o ID: " + id));
  }

  public void delete(Long id) {
    if (!pagamentoRepository.existsById(id)) {
      throw new NoSuchElementException("Pagamento não encontrado com o ID: " + id);
    }
    pagamentoRepository.deleteById(id);
  }

  private void gerarParcelas(Pagamento pagamento) {
    BigDecimal valorTotal = pagamento.getValorTotal();
    BigDecimal entrada = pagamento.getEntrada() != null ? pagamento.getEntrada() : BigDecimal.ZERO;
    int numParcelas = pagamento.getNumeroParcelas();

    if(numParcelas <= 0) {
      throw new IllegalArgumentException("O número de parcelas deve ser maior que zero.");
    }

    BigDecimal valorAParcelar = valorTotal.subtract(entrada);
    BigDecimal valorParcela = valorAParcelar.divide(new BigDecimal(numParcelas), 2, RoundingMode.HALF_UP);
    List<Parcela> parcelas = new ArrayList<>();

    for (int i = 1; i <= numParcelas; i++) {
      Parcela p = new Parcela();
      p.setPagamento(pagamento);
      p.setNumeroParcela(i);
      p.setValorParcela(valorParcela);
      p.setDataVencimento(LocalDate.now().plusMonths(i));
      p.setStatusPagamento(StatusPagamento.PENDENTE);
      parcelas.add(p);
    }
    pagamento.setParcelas(parcelas);
  }

  @Scheduled(cron = "0 0 1 * * ?") // Executa todo dia à 01:00
  @Transactional
  public void verificarEAtualizarParcelasAtrasadas() {
    LocalDate hoje = LocalDate.now();
    List<Parcela> parcelasPendentes = parcelaRepository.findByStatusPagamento(StatusPagamento.PENDENTE);

    for (Parcela parcela : parcelasPendentes) {
      if (parcela.getDataVencimento().isBefore(hoje)) {
        parcela.setStatusPagamento(StatusPagamento.ATRASADO);
        parcelaRepository.save(parcela);
      }
    }
  }

  // --- LÓGICA DOS RELATÓRIOS ---

  public List<Pagamento> getRelatorioPorCliente(Long clienteId) {
    return pagamentoRepository.findByClienteId(clienteId);
  }

  public List<Pagamento> getRelatorioMensal(int ano, int mes) {
    YearMonth anoMes = YearMonth.of(ano, mes);
    LocalDate dataInicio = anoMes.atDay(1);
    LocalDate dataFim = anoMes.atEndOfMonth();

    return pagamentoRepository.findByDataCriacaoBetween(dataInicio, dataFim);
  }

  public List<Pagamento> getRelatorioAnual(int ano) {
    LocalDate dataInicio = LocalDate.of(ano, 1, 1);
    LocalDate dataFim = LocalDate.of(ano, 12, 31);

    return pagamentoRepository.findByDataCriacaoBetween(dataInicio, dataFim);
  }
}
