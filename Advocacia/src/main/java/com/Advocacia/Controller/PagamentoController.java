package com.Advocacia.Controller;

import com.Advocacia.Entity.Pagamento;
import com.Advocacia.Service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamento")
public class PagamentoController {

  @Autowired
  private PagamentoService pagamentoService;

  @PostMapping("/save")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Pagamento> save(@RequestBody Pagamento pagamentoNovo) {
    Pagamento pagamento = pagamentoService.createPagamento(pagamentoNovo);
    return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
  }

  @GetMapping("/findAll")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Pagamento>> findAll() {
    List<Pagamento> pagamentos = pagamentoService.findAll();
    // A lógica para verificar atrasos pode ser chamada aqui ou de forma agendada
    pagamentoService.verificarEAtualizarParcelasAtrasadas();
    return ResponseEntity.status(HttpStatus.OK).body(pagamentos);
  }

  // --- Novos Endpoints para Relatórios ---

  @GetMapping("/relatorio/mensal")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Pagamento>> getRelatorioMensal(@RequestParam int ano, @RequestParam int mes) {
    // A implementação no PagamentoService é necessária
    List<Pagamento> relatorio = pagamentoService.getRelatorioMensal(ano, mes);
    return ResponseEntity.ok(relatorio);
  }

  @GetMapping("/relatorio/anual")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Pagamento>> getRelatorioAnual(@RequestParam int ano) {
    // A implementação no PagamentoService é necessária
    List<Pagamento> relatorio = pagamentoService.getRelatorioAnual(ano);
    return ResponseEntity.ok(relatorio);
  }

  @GetMapping("/relatorio/cliente/{clienteId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Pagamento>> getRelatorioPorCliente(@PathVariable Long clienteId) {
    // A implementação no PagamentoService é necessária
    List<Pagamento> relatorio = pagamentoService.getRelatorioPorCliente(clienteId);
    return ResponseEntity.ok(relatorio);
  }

  // Você pode adicionar outros endpoints como delete, update, findById conforme necessário
}
