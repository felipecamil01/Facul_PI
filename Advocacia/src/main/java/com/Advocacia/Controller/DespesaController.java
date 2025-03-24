package com.Advocacia.Controller;

import com.Advocacia.Entity.Despesa;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/despesa")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;
    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Despesa> save(@RequestBody Despesa despesaNova) {
        Despesa despesa = despesaService.save(despesaNova);
        return ResponseEntity.status(HttpStatus.CREATED).body(despesa);
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Despesa> update(@PathVariable Long id, @RequestBody Despesa despesaAtualizada) {
	  	Despesa despesa = despesaService.update(id, despesaAtualizada);
	    return ResponseEntity.status(HttpStatus.OK).body(despesa);
    }
    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	despesaService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Despesa>> findAll() {
        List<Despesa> despesas = despesaService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(despesas);
    }
    
    @GetMapping("/findById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Despesa> findById(@PathVariable Long id) {
    	Despesa despesa = despesaService.findById(id);
    	return ResponseEntity.status(HttpStatus.OK).body(despesa);
    }
    
    @GetMapping("/findByStatus/{statusPagamento}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Despesa>> findByStatus(@PathVariable StatusPagamento statusPagamento){
        List<Despesa> lista = this.despesaService.findByStatus(statusPagamento);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/findByPagamentoPendente")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Despesa>> findByPagamentoPendente(){
        List<Despesa> lista = this.despesaService.findByPagamentoPendente();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/findByVencimento/{statusPagamento}/{data}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Despesa>> findByVencimento(@PathVariable StatusPagamento statusPagamento, @PathVariable(required = false) LocalDate data) {
        List<Despesa> lista = despesaService.findByVencimento(statusPagamento, data);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/categorias")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> findCategorias(){
        List<String> categorias = despesaService.findCategorias();
        return ResponseEntity.status(HttpStatus.OK).body(categorias);
    }
}