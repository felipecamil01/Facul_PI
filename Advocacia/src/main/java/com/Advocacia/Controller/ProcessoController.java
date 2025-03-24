package com.Advocacia.Controller;

import com.Advocacia.Entity.Processo;
import com.Advocacia.Service.ProcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/processo")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;
    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Processo> save(@RequestBody Processo processo) {
        Processo novoProcesso = processoService.save(processo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProcesso);
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Processo> update(@PathVariable Long id, @RequestBody Processo processoAtualizado) {
    	Processo processo = processoService.update(id, processoAtualizado);
	    return ResponseEntity.status(HttpStatus.OK).body(processo);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	processoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Processo>> findAll() {
        List<Processo> processos = processoService.findAll();
        return ResponseEntity.ok(processos);
    }
  
    @GetMapping("/findById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Processo> findById(@PathVariable Long id) {
    	Processo processo = processoService.findById(id);
    	return ResponseEntity.status(HttpStatus.OK).body(processo);
    }
  
    @GetMapping("/findByNumeroProcesso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Processo>> findByNumeroProcesso(@RequestParam String numero) {
        List<Processo> processos = processoService.findByNumeroProcesso(numero);
        return ResponseEntity.status(HttpStatus.OK).body(processos);
    }
}