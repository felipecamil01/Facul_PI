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
@RequestMapping("/api/financeiro")
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
    public ResponseEntity<Page<Financeiro>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Financeiro> financeiros = financeiroService.findAll(pageable);
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
<<<<<<< HEAD
    @GetMapping("/findByPagamentoPendente")
    public ResponseEntity<List<Financeiro>> findByPagamentoPendente(){
=======
    @GetMapping("/buscarPendentes")
    public ResponseEntity<List<Financeiro>> buscarPendentes(StatusPagamento statusPagamento){
>>>>>>> 8cc5211ab41b7e9938606f86c4bdb708e8001226
        try {
            List<Financeiro> lista = this.financeiroService.findByPagamentoPendente();
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

<<<<<<< HEAD
        @GetMapping("/findByVencimento/{statusPagamento}")
        public ResponseEntity<List<Financeiro>> buscarVencimentos(@PathVariable StatusPagamento statusPagamento,
                                                                  @RequestParam(required = false) LocalDateTime data) {
=======
        @GetMapping("/buscarVencimentos/{statusPagamento}")
        public ResponseEntity<List<Financeiro>> buscarVencimentos(@PathVariable StatusPagamento statusPagamento, @RequestParam(required = false) LocalDateTime data) {
>>>>>>> 8cc5211ab41b7e9938606f86c4bdb708e8001226
            try {
                if (data == null) {
                    data = LocalDateTime.now();
                }

                List<Financeiro> lista = financeiroService.findByVencimento(statusPagamento, data);

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        
}
