package com.Advocacia.Controller;

import com.Advocacia.Entity.Processo;
import com.Advocacia.Service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/processo")
@CrossOrigin("*")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;
  @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Processo> save(@Valid @RequestBody Processo processo) {
        Processo novoProcesso = processoService.save(processo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProcesso);
    }
  @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Processo> update(@PathVariable Long id, @Valid @RequestBody Processo processoAtualizado) {
        try {
            Processo processo = processoService.update(id, processoAtualizado);
            return ResponseEntity.ok(processo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            processoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
  @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<Processo>> findAll() {
        List<Processo> processos = processoService.findAll();
        return ResponseEntity.ok(processos);
    }
  @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<Processo> findById(@PathVariable Long id) {
        return processoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
  @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findByNumeroProcesso")
    public ResponseEntity<List<Processo>> findByNumeroProcesso(@RequestParam String numero) {
        List<Processo> processos = processoService.findByNumeroProcesso(numero);
        return ResponseEntity.ok(processos);
    }

}
