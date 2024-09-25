package com.Advocacia.Controller;

import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Service.FinanceiroService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/financeiros")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @PostMapping
    public ResponseEntity<Financeiro> criarFinanceiro(@Valid @RequestBody Financeiro financeiro) {
        Financeiro novoFinanceiro = financeiroService.salvarFinanceiro(financeiro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFinanceiro);
    }

    @GetMapping
    public ResponseEntity<Page<Financeiro>> listarFinanceiros(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Financeiro> financeiros = financeiroService.listarTodosFinanceiros(pageable);
        return ResponseEntity.ok(financeiros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Financeiro> buscarFinanceiroPorId(@PathVariable Long id) {
        return financeiroService.buscarFinanceiroPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Financeiro> atualizarFinanceiro(@PathVariable Long id, @Valid @RequestBody Financeiro financeiroAtualizado) {
        try {
            Financeiro financeiro = financeiroService.atualizarFinanceiro(id, financeiroAtualizado);
            return ResponseEntity.ok(financeiro);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarFinanceiro(@PathVariable Long id) {
        try {
            financeiroService.deletarFinanceiro(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/buscarStatus/{statusPagamento}")
    public ResponseEntity<List<Financeiro>> buscarPorStatus(@PathVariable StatusPagamento statusPagamento){
        try {
            List<Financeiro> lista = this.financeiroService.buscarPorStatus(statusPagamento);
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/buscarPendentes")
    public ResponseEntity<List<Financeiro>> buscarPendentes(){
        try {
            List<Financeiro> lista = this.financeiroService.buscarPagamentoPendente();
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

        @GetMapping("/buscarVencimentos/{statusPagamento}")
        public ResponseEntity<List<Financeiro>> buscarVencimentos(@PathVariable StatusPagamento statusPagamento,
                                                                  @RequestParam(required = false) LocalDateTime data) {
            try {
                // Se a data não for fornecida, usa a data atual
                if (data == null) {
                    data = LocalDateTime.now();
                }

                // Supondo que você tenha um serviço para buscar os vencimentos
                List<Financeiro> lista = financeiroService.buscarVencimentos(statusPagamento, data);

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }



}