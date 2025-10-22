package com.Advocacia.Service;

import com.Advocacia.DTO.RelatorioMensalDTO;
import com.Advocacia.DTO.RelatorioMensalItemDTO;
import com.Advocacia.Entity.Agenda;
import com.Advocacia.Entity.Pagamento;
import com.Advocacia.Entity.Parcela;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Enum.TipoPagamento;
import com.Advocacia.Repository.AgendaRepository;
import com.Advocacia.Repository.PagamentoRepository;
import com.Advocacia.Repository.ParcelaRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
public class PagamentoService {

  private static final Locale LOCALE_BR = new Locale("pt", "BR");
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(LOCALE_BR);

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
      gerarParcelaUnica(pagamento);
    }

    Pagamento salvo = pagamentoRepository.save(pagamento);
    criarEventosAgenda(salvo);
    return salvo;
  }

  @Transactional
  public Pagamento update(Long id, Pagamento dados) {
    Pagamento existente = pagamentoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Pagamento nao encontrado com o ID: " + id));

    existente.setCliente(dados.getCliente());
    existente.setTipoPagamento(dados.getTipoPagamento());
    existente.setValorTotal(dados.getValorTotal());
    existente.setEntrada(dados.getEntrada());
    existente.setNumeroParcelas(dados.getNumeroParcelas());
    existente.setObservacao(dados.getObservacao());
    existente.setDataPagamento(dados.getDataPagamento());

    if (existente.getParcelas() != null) {
      for (Parcela antiga : new ArrayList<>(existente.getParcelas())) {
        if (antiga.getId() != null) {
          agendaRepository.deleteByParcelaId(antiga.getId());
        }
      }
      existente.getParcelas().clear();
    }

    if (existente.getTipoPagamento() == TipoPagamento.PARCELADO) {
      gerarParcelas(existente);
    } else {
      gerarParcelaUnica(existente);
    }

    Pagamento salvo = pagamentoRepository.save(existente);
    criarEventosAgenda(salvo);
    return salvo;
  }

  public List<Pagamento> findAll() {
    return pagamentoRepository.findAll();
  }

  public Pagamento findById(Long id) {
    return pagamentoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Pagamento nao encontrado com o ID: " + id));
  }

  public void delete(Long id) {
    if (!pagamentoRepository.existsById(id)) {
      throw new NoSuchElementException("Pagamento nao encontrado com o ID: " + id);
    }
    pagamentoRepository.deleteById(id);
  }

  private void gerarParcelaUnica(Pagamento pagamento) {
    Parcela unicaParcela = new Parcela();
    unicaParcela.setPagamento(pagamento);
    unicaParcela.setNumeroParcela(1);
    unicaParcela.setValorParcela(pagamento.getValorTotal());
    LocalDate base = pagamento.getDataPagamento() != null ? pagamento.getDataPagamento() : LocalDate.now();
    unicaParcela.setDataVencimento(base);
    unicaParcela.setStatusPagamento(StatusPagamento.PAGO);
    pagamento.setParcelas(List.of(unicaParcela));
    pagamento.setNumeroParcelas(1);
  }

  private void gerarParcelas(Pagamento pagamento) {
    BigDecimal valorTotal = safeBigDecimal(pagamento.getValorTotal());
    BigDecimal entrada = safeBigDecimal(pagamento.getEntrada());
    int numeroParcelas = pagamento.getNumeroParcelas() != null ? pagamento.getNumeroParcelas() : 0;

    if (numeroParcelas <= 0) {
      throw new IllegalArgumentException("O numero de parcelas deve ser maior que zero.");
    }

    BigDecimal valorAParcelar = valorTotal.subtract(entrada);
    if (valorAParcelar.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("O valor da entrada nao pode ser maior que o valor total.");
    }

    BigDecimal valorBase = valorAParcelar.divide(BigDecimal.valueOf(numeroParcelas), 2, RoundingMode.HALF_UP);
    BigDecimal valorTotalArredondado = valorBase.multiply(BigDecimal.valueOf(numeroParcelas));
    BigDecimal diferenca = valorAParcelar.subtract(valorTotalArredondado).setScale(2, RoundingMode.HALF_UP);

    List<Parcela> parcelas = new ArrayList<>();
    LocalDate dataBase = pagamento.getDataPagamento() != null ? pagamento.getDataPagamento() : LocalDate.now();

    for (int i = 1; i <= numeroParcelas; i++) {
      Parcela parcela = new Parcela();
      parcela.setPagamento(pagamento);
      parcela.setNumeroParcela(i);

      BigDecimal valorParcela = valorBase;
      if (i == numeroParcelas && diferenca.compareTo(BigDecimal.ZERO) != 0) {
        valorParcela = valorParcela.add(diferenca);
      }

      parcela.setValorParcela(valorParcela.setScale(2, RoundingMode.HALF_UP));
      parcela.setDataVencimento(dataBase.plusMonths(i - 1));
      parcela.setStatusPagamento(StatusPagamento.PENDENTE);
      parcelas.add(parcela);
    }

    pagamento.setParcelas(parcelas);
  }

  private BigDecimal safeBigDecimal(BigDecimal value) {
    return value != null ? value : BigDecimal.ZERO;
  }

  private void criarEventosAgenda(Pagamento pagamento) {
    if (pagamento.getParcelas() == null) {
      return;
    }

    for (Parcela parcela : pagamento.getParcelas()) {
      Agenda agenda = new Agenda();
      LocalDateTime dataEvento = LocalDateTime.of(parcela.getDataVencimento(), LocalTime.of(9, 0));
      agenda.setData(dataEvento);
      agenda.setDescricao("Vencimento parcela " + parcela.getNumeroParcela() + " - Cliente " + pagamento.getCliente().getNome());
      agenda.setTipo("Pagamentos");
      agenda.setProcesso(null);
      agenda.setParcela(parcela);
      agendaRepository.save(agenda);
    }
  }

  @Scheduled(cron = "0 0 1 * * ?")
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

  @Transactional(readOnly = true)
  public RelatorioMensalDTO gerarRelatorioMensalDetalhado(int ano, int mes, Long clienteId, StatusPagamento statusFiltro) {
    YearMonth periodo = YearMonth.of(ano, mes);
    LocalDate dataInicio = periodo.atDay(1);
    LocalDate dataFim = periodo.atEndOfMonth();

    List<Parcela> parcelas = parcelaRepository.buscarParcelasParaRelatorio(dataInicio, dataFim, statusFiltro, clienteId);
    List<RelatorioMensalItemDTO> itens = new ArrayList<>();

    BigDecimal totalGeral = BigDecimal.ZERO;
    BigDecimal totalPago = BigDecimal.ZERO;
    BigDecimal totalAtrasado = BigDecimal.ZERO;
    BigDecimal totalPendente = BigDecimal.ZERO;

    for (Parcela parcela : parcelas) {
      BigDecimal valor = safeBigDecimal(parcela.getValorParcela());
      totalGeral = totalGeral.add(valor);

      if (parcela.getStatusPagamento() == StatusPagamento.PAGO) {
        totalPago = totalPago.add(valor);
      } else if (parcela.getStatusPagamento() == StatusPagamento.ATRASADO) {
        totalAtrasado = totalAtrasado.add(valor);
      } else if (parcela.getStatusPagamento() == StatusPagamento.PENDENTE) {
        totalPendente = totalPendente.add(valor);
      }

      itens.add(RelatorioMensalItemDTO.builder()
          .pagamentoId(parcela.getPagamento().getId())
          .parcelaId(parcela.getId())
          .clienteNome(parcela.getPagamento().getCliente().getNome())
          .numeroParcela(parcela.getNumeroParcela())
          .dataVencimento(parcela.getDataVencimento())
          .statusPagamento(parcela.getStatusPagamento())
          .valorParcela(valor.setScale(2, RoundingMode.HALF_UP))
          .tipoPagamento(parcela.getPagamento().getTipoPagamento())
          .build());
    }

    String clienteNome = null;
    if (clienteId != null && !parcelas.isEmpty()) {
      clienteNome = parcelas.get(0).getPagamento().getCliente().getNome();
    }

    return RelatorioMensalDTO.builder()
        .ano(ano)
        .mes(mes)
        .clienteId(clienteId)
        .clienteNome(clienteNome)
        .filtroStatus(statusFiltro)
        .totalGeral(totalGeral.setScale(2, RoundingMode.HALF_UP))
        .totalPago(totalPago.setScale(2, RoundingMode.HALF_UP))
        .totalAtrasado(totalAtrasado.setScale(2, RoundingMode.HALF_UP))
        .totalPendente(totalPendente.setScale(2, RoundingMode.HALF_UP))
        .itens(itens)
        .build();
  }

  public byte[] gerarRelatorioMensalCsv(RelatorioMensalDTO relatorio) {
    StringBuilder sb = new StringBuilder();
    sb.append("Cliente;PagamentoId;Parcela;DataVencimento;Status;Valor\n");

    for (RelatorioMensalItemDTO item : relatorio.getItens()) {
      sb.append(escapeCsv(item.getClienteNome())).append(";")
          .append(item.getPagamentoId()).append(";")
          .append(item.getNumeroParcela()).append(";")
          .append(item.getDataVencimento()).append(";")
          .append(item.getStatusPagamento()).append(";")
          .append((item.getValorParcela() != null ? item.getValorParcela() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP))
          .append("\n");
    }

    sb.append("\n");
    sb.append("Total Geral;").append(relatorio.getTotalGeral()).append("\n");
    sb.append("Total Pago;").append(relatorio.getTotalPago()).append("\n");
    sb.append("Total Atrasado;").append(relatorio.getTotalAtrasado()).append("\n");
    sb.append("Total Pendente;").append(relatorio.getTotalPendente()).append("\n");

    return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
  }

  private String escapeCsv(String value) {
    if (value == null) {
      return "";
    }
    String sanitized = value.replace("\"", "\"\"");
    if (sanitized.contains(";") || sanitized.contains("\"")) {
      return "\"" + sanitized + "\"";
    }
    return sanitized;
  }

  public byte[] gerarRelatorioMensalPdf(RelatorioMensalDTO relatorio) {
    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage(PDRectangle.A4);
      document.addPage(page);

      PDPageContentStream content = new PDPageContentStream(document, page);
      try {
        float margin = 40f;
        float y = page.getMediaBox().getHeight() - margin;

        y = drawTitleSection(content, relatorio, margin, y);
        y -= 10;
        y = drawTableHeader(page, content, margin, y);

        for (RelatorioMensalItemDTO item : relatorio.getItens()) {
          if (y <= margin + 40) {
            content.close();
            page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            content = new PDPageContentStream(document, page);
            y = page.getMediaBox().getHeight() - margin;
            y = drawTableHeader(page, content, margin, y);
          }
          y = drawTableRow(content, margin, y, item);
        }

        y -= 20;
        drawText(content, margin, y, "Total geral: " + CURRENCY_FORMAT.format(relatorio.getTotalGeral()), 11, true);
        y -= 14;
        drawText(content, margin, y, "Total pago: " + CURRENCY_FORMAT.format(relatorio.getTotalPago()), 10, false);
        y -= 14;
        drawText(content, margin, y, "Total atrasado: " + CURRENCY_FORMAT.format(relatorio.getTotalAtrasado()), 10, false);
        y -= 14;
        drawText(content, margin, y, "Total pendente: " + CURRENCY_FORMAT.format(relatorio.getTotalPendente()), 10, false);
      } finally {
        content.close();
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      document.save(outputStream);
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException("Erro ao gerar PDF do relatorio mensal.", e);
    }
  }

  private float drawTitleSection(PDPageContentStream content,
                                 RelatorioMensalDTO relatorio,
                                 float margin,
                                 float y) throws IOException {
    drawText(content, margin, y, "Relatorio Mensal de Pagamentos", 16, true);
    y -= 18;

    String periodo = String.format("Periodo: %d/%02d", relatorio.getAno(), relatorio.getMes());
    drawText(content, margin, y, periodo, 11, false);
    y -= 14;

    if (relatorio.getClienteNome() != null) {
      drawText(content, margin, y, "Cliente: " + relatorio.getClienteNome(), 11, false);
      y -= 14;
    }

    if (relatorio.getFiltroStatus() != null) {
      drawText(content, margin, y, "Filtro status: " + relatorio.getFiltroStatus(), 11, false);
      y -= 14;
    }

    return y;
  }

  private float drawTableHeader(PDPage page,
                                PDPageContentStream content,
                                float margin,
                                float y) throws IOException {
    drawText(content, margin, y, "Cliente", 11, true);
    drawText(content, margin + 240, y, "Parcela", 11, true);
    drawText(content, margin + 300, y, "Vencimento", 11, true);
    drawText(content, margin + 390, y, "Status", 11, true);
    drawText(content, margin + 460, y, "Valor", 11, true);
    y -= 12;

    content.setStrokingColor(0, 0, 0);
    content.moveTo(margin, y);
    content.lineTo(page.getMediaBox().getWidth() - margin, y);
    content.stroke();
    y -= 8;

    return y;
  }

  private float drawTableRow(PDPageContentStream content,
                             float margin,
                             float y,
                             RelatorioMensalItemDTO item) throws IOException {
    drawText(content, margin, y, truncate(item.getClienteNome(), 32), 10, false);
    String parcelaInfo = String.format("%d/%s", item.getNumeroParcela(),
        item.getTipoPagamento() == null ? "" : item.getTipoPagamento().name());
    drawText(content, margin + 240, y, parcelaInfo, 10, false);
    drawText(content, margin + 300, y, formatData(item.getDataVencimento()), 10, false);
    drawText(content, margin + 390, y, item.getStatusPagamento().name(), 10, false);
    drawText(content, margin + 460, y, CURRENCY_FORMAT.format(item.getValorParcela()), 10, false);
    return y - 14;
  }

  private String formatData(LocalDate data) {
    if (data == null) {
      return "-";
    }
    return String.format("%02d/%02d/%d", data.getDayOfMonth(), data.getMonthValue(), data.getYear());
  }

  private String truncate(String texto, int maxLength) {
    if (texto == null) {
      return "";
    }
    if (texto.length() <= maxLength) {
      return texto;
    }
    return texto.substring(0, maxLength - 3) + "...";
  }

  private void drawText(PDPageContentStream content,
                        float x,
                        float y,
                        String text,
                        float fontSize,
                        boolean bold) throws IOException {
    content.beginText();
    content.setFont(bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, fontSize);
    content.newLineAtOffset(x, y);
    content.showText(text != null ? text : "");
    content.endText();
  }
}
