package com.Advocacia.Controller;

import com.Advocacia.Entity.Documento;
import com.Advocacia.Service.DocumentoService;
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
@RequestMapping("/api/documento")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/save")
    public ResponseEntity<Documento> save(@Valid @RequestBody Documento documentacao) {
        Documento novaDocumentacao = documentoService.save(documentacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaDocumentacao);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Documento> update(@PathVariable Long id, @Valid @RequestBody Documento documentoAtualizada) {
        try {
            Documento documentacao = documentoService.update(id, documentoAtualizada);
            return ResponseEntity.ok(documentacao);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            documentoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/findAll")
    public ResponseEntity<Page<Documento>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Documento> documentacoes = documentoService.findAll(pageable);
        return ResponseEntity.ok(documentacoes);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Documento> findById(@PathVariable Long id) {
        return documentoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
}
