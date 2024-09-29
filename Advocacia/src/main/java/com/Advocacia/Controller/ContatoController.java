package com.Advocacia.Controller;

import com.Advocacia.Entity.Contato;
import com.Advocacia.Service.ContatoService;
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

@RestController
@RequestMapping("/api/contato")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @PostMapping("/save")
    public ResponseEntity<Contato> save(@Valid @RequestBody Contato contato) {
        Contato novoContato = contatoService.save(contato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Contato> update(@PathVariable Long id, @Valid @RequestBody Contato contatoAtualizado) {
        try {
            Contato contato = contatoService.update(id, contatoAtualizado);
            return ResponseEntity.ok(contato);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

<<<<<<< HEAD
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
=======
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarContato(@PathVariable Long id) {
>>>>>>> 8cc5211ab41b7e9938606f86c4bdb708e8001226
        try {
            contatoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("findAll")
    public ResponseEntity<Page<Contato>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Contato> contatos = contatoService.findAll(pageable);
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Contato> findById(@PathVariable Long id) {
        return contatoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
}
