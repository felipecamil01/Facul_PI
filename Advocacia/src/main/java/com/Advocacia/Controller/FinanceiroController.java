package com.Advocacia.Controller;

import com.Advocacia.Entity.Financeiro;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Service.FinanceiroService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/financeiro")
@CrossOrigin("*")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @PostMapping("/save")
    public ResponseEntity<Financeiro> save(@Valid @RequestBody Financeiro financeiro) {
        Financeiro novoFinanceiro = financeiroService.save(financeiro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFinanceiro);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Financeiro> update(@PathVariable Long id, @Valid @RequestBody Financeiro financeiroAtualizado) {
        try {
            Financeiro financeiro = financeiroService.update(id, financeiroAtualizado);
            return ResponseEntity.ok(financeiro);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            financeiroService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Financeiro>> findAll() {
        List<Financeiro> financeiros = financeiroService.findAll();
        return ResponseEntity.ok(financeiros);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Financeiro> findById(@PathVariable Long id) {
        return financeiroService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByStatus/{statusPagamento}")
    public ResponseEntity<List<Financeiro>> findByStatus(@PathVariable StatusPagamento statusPagamento){
        try {
            List<Financeiro> lista = this.financeiroService.findByStatus(statusPagamento);
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/findByPagamentoPendente")
    public ResponseEntity<List<Financeiro>> findByPagamentoPendente(){
        try {
            List<Financeiro> lista = this.financeiroService.findByPagamentoPendente();
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/findByVencimento/{statusPagamento}/{data}")
    public ResponseEntity<List<Financeiro>> findByVencimento(@PathVariable StatusPagamento statusPagamento,
                                                             @PathVariable(required = false) LocalDate data) {
        try {
            List<Financeiro> lista = financeiroService.findByVencimento(statusPagamento, data);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
