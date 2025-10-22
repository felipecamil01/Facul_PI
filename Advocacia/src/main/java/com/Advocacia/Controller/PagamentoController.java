package com.Advocacia.Controller;

import com.Advocacia.DTO.RelatorioMensalDTO;
import com.Advocacia.Entity.Pagamento;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Service.PagamentoService;
import com.Advocacia.Service.ParcelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/pagamento")
public class PagamentoController {

  @Autowired
  private PagamentoService pagamentoService;

  @Autowired
  private ParcelaService parcelaService;

  @PostMapping("/save")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Pagamento> save(@RequestBody Pagamento pagamentoNovo) {
    Pagamento pagamento = pagamentoService.createPagamento(pagamentoNovo);
    return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
  }

  @GetMapping("/findAll")
  public ResponseEntity<List<Pagamento>> findAll() {
    pagamentoService.verificarEAtualizarParcelasAtrasadas();
    List<Pagamento> pagamentos = pagamentoService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(pagamentos);
  }

  @GetMapping("/findById/{id}")
  public ResponseEntity<Pagamento> findById(@PathVariable Long id) {
    Pagamento p = pagamentoService.findById(id);
    return ResponseEntity.ok(p);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Pagamento> update(@PathVariable Long id, @RequestBody Pagamento pagamentoAtualizado) {
    Pagamento p = pagamentoService.update(id, pagamentoAtualizado);
    return ResponseEntity.ok(p);
  }

  @PutMapping("/{id}/confirmar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Pagamento> confirmarPagamento(@PathVariable Long id,
                                                       @RequestParam(required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                       LocalDate dataPagamento) {
    Pagamento atualizado = parcelaService.confirmarPagamentoCompleto(id, dataPagamento);
    return ResponseEntity.ok(atualizado);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    pagamentoService.delete(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/relatorio/mensal")
  public ResponseEntity<RelatorioMensalDTO> getRelatorioMensal(@RequestParam int ano,
                                                               @RequestParam int mes,
                                                               @RequestParam(required = false) Long clienteId,
                                                               @RequestParam(required = false) StatusPagamento status) {
    RelatorioMensalDTO relatorio = pagamentoService.gerarRelatorioMensalDetalhado(ano, mes, clienteId, status);
    return ResponseEntity.ok(relatorio);
  }

  @GetMapping("/relatorio/mensal/export")
  public ResponseEntity<byte[]> exportarRelatorioMensal(@RequestParam int ano,
                                                        @RequestParam int mes,
                                                        @RequestParam String formato,
                                                        @RequestParam(required = false) Long clienteId,
                                                        @RequestParam(required = false) StatusPagamento status) {
    RelatorioMensalDTO relatorio = pagamentoService.gerarRelatorioMensalDetalhado(ano, mes, clienteId, status);

    String formatoNormalizado = formato == null ? "" : formato.toLowerCase();
    String nomeArquivoBase = String.format("relatorio-mensal-%d-%02d", ano, mes);

    if ("pdf".equals(formatoNormalizado)) {
      byte[] pdf = pagamentoService.gerarRelatorioMensalPdf(relatorio);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomeArquivoBase + ".pdf")
          .contentType(MediaType.APPLICATION_PDF)
          .body(pdf);
    }

    if ("csv".equals(formatoNormalizado)) {
      byte[] csv = pagamentoService.gerarRelatorioMensalCsv(relatorio);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomeArquivoBase + ".csv")
          .contentType(MediaType.valueOf("text/csv"))
          .body(csv);
    }

    return ResponseEntity.badRequest().build();
  }

  @GetMapping("/relatorio/anual")
  public ResponseEntity<List<Pagamento>> getRelatorioAnual(@RequestParam int ano) {
    List<Pagamento> relatorio = pagamentoService.getRelatorioAnual(ano);
    return ResponseEntity.ok(relatorio);
  }

  @GetMapping("/relatorio/cliente/{clienteId}")
  public ResponseEntity<List<Pagamento>> getRelatorioPorCliente(@PathVariable Long clienteId) {
    List<Pagamento> relatorio = pagamentoService.getRelatorioPorCliente(clienteId);
    return ResponseEntity.ok(relatorio);
  }

  @GetMapping("/search")
  public ResponseEntity<List<Pagamento>> searchByClienteNome(@RequestParam String nome) {
    List<Pagamento> lista = pagamentoService.searchByClienteNome(nome);
    return ResponseEntity.ok(lista);
  }
}
