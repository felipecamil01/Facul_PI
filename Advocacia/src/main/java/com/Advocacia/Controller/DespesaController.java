package com.Advocacia.Controller;

import com.Advocacia.Entity.Despesa;
import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Service.DespesaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/despesa")
@CrossOrigin("*")
public class DespesaController {

    @Autowired
    private DespesaService financeiroService;
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Despesa> save(@Valid @RequestBody Despesa financeiro) {
        Despesa novoFinanceiro = financeiroService.save(financeiro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFinanceiro);
    }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Despesa> update(@PathVariable Long id, @Valid @RequestBody Despesa financeiroAtualizado) {
        try {
            Despesa financeiro = financeiroService.update(id, financeiroAtualizado);
            return ResponseEntity.ok(financeiro);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            financeiroService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<Despesa>> findAll() {
        List<Despesa> financeiros = financeiroService.findAll();
        return ResponseEntity.ok(financeiros);
    }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<Despesa> findById(@PathVariable Long id) {
        return financeiroService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/findByStatus/{statusPagamento}")
    public ResponseEntity<List<Despesa>> findByStatus(@PathVariable StatusPagamento statusPagamento){
        try {
            List<Despesa> lista = this.financeiroService.findByStatus(statusPagamento);
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/findByPagamentoPendente")
    public ResponseEntity<List<Despesa>> findByPagamentoPendente(){
        try {
            List<Despesa> lista = this.financeiroService.findByPagamentoPendente();
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/findByVencimento/{statusPagamento}/{data}")
    public ResponseEntity<List<Despesa>> findByVencimento(@PathVariable StatusPagamento statusPagamento,
                                                             @PathVariable(required = false) LocalDate data) {
        try {
            List<Despesa> lista = financeiroService.findByVencimento(statusPagamento, data);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> findCategorias(){
    	List<String> categorias = financeiroService.findCategorias();
    	return ResponseEntity.ok(categorias);
    }

}


