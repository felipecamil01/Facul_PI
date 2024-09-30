package com.Advocacia.Controller;

import com.Advocacia.Entity.Endereco;
import com.Advocacia.Service.EnderecoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping("/save")
    public ResponseEntity<Endereco> save(@Valid @RequestBody Endereco endereco) {
        Endereco novoEndereco = enderecoService.save(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Endereco> update(@PathVariable Long id, @Valid @RequestBody Endereco enderecoAtualizado) {
        try {
            Endereco endereco = enderecoService.update(id, enderecoAtualizado);
            return ResponseEntity.ok(endereco);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            enderecoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Endereco>> findAll() {
        List<Endereco> enderecos = enderecoService.findAll();
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Endereco> findById(@PathVariable Long id) {
        return enderecoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
}
