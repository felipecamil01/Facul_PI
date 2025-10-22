package com.Advocacia.Service;

import com.Advocacia.Entity.Parcela;
import com.Advocacia.Entity.Pagamento;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Repository.AgendaRepository;
import com.Advocacia.Repository.PagamentoRepository;
import com.Advocacia.Repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ParcelaService {

  @Autowired
  private ParcelaRepository parcelaRepository;

  @Autowired
  private PagamentoRepository pagamentoRepository;

  @Autowired
  private AgendaRepository agendaRepository;

  @Transactional
  public Parcela marcarComoPago(Long parcelaId) {
    Parcela parcela = parcelaRepository.findById(parcelaId)
        .orElseThrow(() -> new NoSuchElementException("Parcela nao encontrada para o id " + parcelaId));

    if (parcela.getStatusPagamento() != StatusPagamento.PAGO) {
      parcela.setStatusPagamento(StatusPagamento.PAGO);
      parcela = parcelaRepository.save(parcela);
      agendaRepository.deleteByParcelaId(parcelaId);
      atualizarResumoPagamento(parcela.getPagamento().getId());
    }

    return parcela;
  }

  @Transactional
  public Pagamento confirmarPagamentoCompleto(Long pagamentoId, LocalDate dataConfirmacao) {
    Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
        .orElseThrow(() -> new NoSuchElementException("Pagamento nao encontrado para o id " + pagamentoId));

    LocalDate dataPagamento = dataConfirmacao != null
        ? dataConfirmacao
        : (pagamento.getDataPagamento() != null ? pagamento.getDataPagamento() : LocalDate.now());

    List<Parcela> parcelas = parcelaRepository.findByPagamentoId(pagamentoId);
    boolean houveAlteracao = false;
    for (Parcela parcela : parcelas) {
      if (parcela.getStatusPagamento() != StatusPagamento.PAGO) {
        parcela.setStatusPagamento(StatusPagamento.PAGO);
        houveAlteracao = true;
      }
      if (parcela.getId() != null) {
        agendaRepository.deleteByParcelaId(parcela.getId());
      }
    }

    if (houveAlteracao) {
      parcelaRepository.saveAll(parcelas);
    }

    pagamento.setDataPagamento(dataPagamento);
    pagamento.setParcelas(parcelas);
    return pagamentoRepository.save(pagamento);
  }

  private void atualizarResumoPagamento(Long pagamentoId) {
    List<Parcela> parcelas = parcelaRepository.findByPagamentoId(pagamentoId);
    boolean todasPagas = parcelas.stream().allMatch(parcela -> parcela.getStatusPagamento() == StatusPagamento.PAGO);

    Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
        .orElseThrow(() -> new NoSuchElementException("Pagamento nao encontrado para o id " + pagamentoId));

    if (todasPagas && pagamento.getDataPagamento() == null) {
      pagamento.setDataPagamento(LocalDate.now());
      pagamento.setParcelas(parcelas);
      pagamentoRepository.save(pagamento);
    }
  }
}
