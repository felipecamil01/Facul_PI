package com.Advocacia.Service;

import com.Advocacia.Entity.Pagamento;
import com.Advocacia.Entity.Parcela;
import com.Advocacia.Entity.Agenda;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Enum.TipoPagamento;
import com.Advocacia.Repository.PagamentoRepository;
import com.Advocacia.Repository.ParcelaRepository;
import com.Advocacia.Repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  @Autowired
  private AgendaRepository agendaRepository;

  @Transactional
  public Pagamento createPagamento(Pagamento pagamento) {
    if (pagamento.getTipoPagamento() == TipoPagamento.PARCELADO) {
      gerarParcelas(pagamento);
    } else {
      Parcela unicaParcela = new Parcela();
      unicaParcela.setPagamento(pagamento);
      unicaParcela.setNumeroParcela(1);
      unicaParcela.setValorParcela(pagamento.getValorTotal());
      // Pagamentos à vista são considerados quitados no ato
      LocalDate base = pagamento.getDataPagamento() != null ? pagamento.getDataPagamento() : LocalDate.now();
      unicaParcela.setDataVencimento(base);
      unicaParcela.setStatusPagamento(StatusPagamento.PAGO);
      pagamento.setParcelas(List.of(unicaParcela));
    }

    Pagamento salvo = pagamentoRepository.save(pagamento);

    // Criar entradas de agenda para cada parcela (vencimentos)
    if (salvo.getParcelas() != null) {
      for (Parcela p : salvo.getParcelas()) {
        Agenda ag = new Agenda();
        LocalDateTime dt = LocalDateTime.of(p.getDataVencimento(), LocalTime.of(9, 0));
        ag.setData(dt);
        ag.setDescricao("Vencimento parcela " + p.getNumeroParcela() + " - Cliente " + salvo.getCliente().getNome());
        ag.setTipo("Pagamentos");
        ag.setProcesso(null);
        ag.setParcela(p);
        agendaRepository.save(ag);
      }
    }

    return salvo;
  }

  @Transactional
  public Pagamento update(Long id, Pagamento dados) {
    Pagamento existente = pagamentoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Pagamento não encontrado com o ID: " + id));

    existente.setCliente(dados.getCliente());
    existente.setTipoPagamento(dados.getTipoPagamento());
    existente.setValorTotal(dados.getValorTotal());
    existente.setEntrada(dados.getEntrada());
    existente.setNumeroParcelas(dados.getNumeroParcelas());
    existente.setObservacao(dados.getObservacao());
    existente.setDataPagamento(dados.getDataPagamento());

    // Remover agendas vinculadas às parcelas atuais (para evitar duplicidade)
    if (existente.getParcelas() != null) {
      for (Parcela antiga : existente.getParcelas()) {
        if (antiga.getId() != null) {
          agendaRepository.deleteByParcelaId(antiga.getId());
        }
      }
      // Limpa a coleção para acionar orphanRemoval nas parcelas antigas
      existente.getParcelas().clear();
    }

    // Recalcula parcelas conforme o tipo
    if (existente.getTipoPagamento() == TipoPagamento.PARCELADO) {
      gerarParcelas(existente);
    } else {
      Parcela unicaParcela = new Parcela();
      unicaParcela.setPagamento(existente);
      unicaParcela.setNumeroParcela(1);
      unicaParcela.setValorParcela(existente.getValorTotal());
      LocalDate base = existente.getDataPagamento() != null ? existente.getDataPagamento() : LocalDate.now();
      unicaParcela.setDataVencimento(base);
      unicaParcela.setStatusPagamento(StatusPagamento.PAGO);
      existente.setParcelas(List.of(unicaParcela));
    }

    Pagamento salvo = pagamentoRepository.save(existente);

    // Recriar agendas com base nas novas parcelas
    if (salvo.getParcelas() != null) {
      for (Parcela p : salvo.getParcelas()) {
        Agenda ag = new Agenda();
        LocalDateTime dt = LocalDateTime.of(p.getDataVencimento(), LocalTime.of(9, 0));
        ag.setData(dt);
        ag.setDescricao("Vencimento parcela " + p.getNumeroParcela() + " - Cliente " + salvo.getCliente().getNome());
        ag.setTipo("Pagamentos");
        ag.setProcesso(null);
        ag.setParcela(p);
        agendaRepository.save(ag);
      }
    }

    return salvo;
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

    LocalDate base = pagamento.getDataPagamento() != null ? pagamento.getDataPagamento() : LocalDate.now();
    for (int i = 1; i <= numParcelas; i++) {
      Parcela p = new Parcela();
      p.setPagamento(pagamento);
      p.setNumeroParcela(i);
      p.setValorParcela(valorParcela);
      p.setDataVencimento(base.plusMonths(i - 1));
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

  public List<Pagamento> searchByClienteNome(String nome) {
    return pagamentoRepository.findByClienteNomeContainingIgnoreCase(nome);
  }
}
