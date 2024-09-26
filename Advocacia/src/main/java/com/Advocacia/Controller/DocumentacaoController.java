package com.Advocacia.Controller;

import com.Advocacia.Entity.Documentos;
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

import java.util.List;

@RestController
@RequestMapping("/documentacoes")
public class DocumentacaoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping
    public ResponseEntity<Documentos> criarDocumentacao(@Valid @RequestBody Documentos documentacao) {
        Documentos novaDocumentacao = documentoService.salvarDocumentacao(documentacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaDocumentacao);
    }

    @GetMapping
    public ResponseEntity<Page<Documentos>> listarDocumentacoes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Documentos> documentacoes = documentoService.listarTodaDocumentacao(pageable);
        return ResponseEntity.ok(documentacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documentos> buscarDocumentacaoPorId(@PathVariable Long id) {
        return documentoService.buscarDocumentacaoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Documentos> atualizarDocumentacao(@PathVariable Long id, @Valid @RequestBody Documentos documentoAtualizada) {
        try {
            Documentos documentacao = documentoService.atualizarDocumentacao(id, documentoAtualizada);
            return ResponseEntity.ok(documentacao);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDocumentacao(@PathVariable Long id) {
        try {
            documentoService.deletarDocumentacao(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}