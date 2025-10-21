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
@CrossOrigin("*")
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
  public ResponseEntity<List<Pagamento>> findAll() {
    List<Pagamento> pagamentos = pagamentoService.findAll();
    // A lógica para verificar atrasos pode ser chamada aqui ou de forma agendada
    pagamentoService.verificarEAtualizarParcelasAtrasadas();
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

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    pagamentoService.delete(id);
    return ResponseEntity.ok().build();
  }

  // --- Novos Endpoints para Relatórios ---

  @GetMapping("/relatorio/mensal")
  public ResponseEntity<List<Pagamento>> getRelatorioMensal(@RequestParam int ano, @RequestParam int mes) {
    // A implementação no PagamentoService é necessária
    List<Pagamento> relatorio = pagamentoService.getRelatorioMensal(ano, mes);
    return ResponseEntity.ok(relatorio);
  }

  @GetMapping("/relatorio/anual")
  public ResponseEntity<List<Pagamento>> getRelatorioAnual(@RequestParam int ano) {
    // A implementação no PagamentoService é necessária
    List<Pagamento> relatorio = pagamentoService.getRelatorioAnual(ano);
    return ResponseEntity.ok(relatorio);
  }

  @GetMapping("/relatorio/cliente/{clienteId}")
  public ResponseEntity<List<Pagamento>> getRelatorioPorCliente(@PathVariable Long clienteId) {
    // A implementação no PagamentoService é necessária
    List<Pagamento> relatorio = pagamentoService.getRelatorioPorCliente(clienteId);
    return ResponseEntity.ok(relatorio);
  }

  @GetMapping("/search")
  public ResponseEntity<List<Pagamento>> searchByClienteNome(@RequestParam String nome) {
    List<Pagamento> lista = pagamentoService.searchByClienteNome(nome);
    return ResponseEntity.ok(lista);
  }

  // Você pode adicionar outros endpoints como delete, update, findById conforme necessário
}
