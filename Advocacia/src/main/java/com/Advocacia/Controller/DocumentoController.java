package com.Advocacia.Controller;
import com.Advocacia.Entity.Documento;
import com.Advocacia.Service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documento")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Documento> save(@RequestBody Documento documentoNovo) {
        Documento documento = documentoService.save(documentoNovo);
        return ResponseEntity.status(HttpStatus.CREATED).body(documento);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Documento> update(@PathVariable Long id, @RequestBody Documento documentoAtualizado) {
        Documento documento = documentoService.update(id, documentoAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(documento);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ADMIN','ADVOGADO','SECRETARIA')")
    public ResponseEntity<List<Documento>> findAll() {
        List<Documento> documentos = documentoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(documentos);
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ADVOGADO','SECRETARIA')")
    public ResponseEntity<Documento> findById(@PathVariable Long id) {
        Documento documento = documentoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(documento);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN','ADVOGADO','SECRETARIA')")
    public ResponseEntity<List<Documento>> findByCliente(@PathVariable Long clienteId) {
        List<Documento> documentos = documentoService.findByCliente(clienteId);
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN','ADVOGADO','SECRETARIA')")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        Documento documento = documentoService.findById(id);
        byte[] arquivo = documentoService.downloadArquivo(id);
        String nomeArquivo = documento.getNomeArquivo() != null ? documento.getNomeArquivo() : documento.getTitulo() + ".pdf";
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(arquivo);
    }

    @GetMapping("/findStatusDocumento")
    @PreAuthorize("hasAnyRole('ADMIN','ADVOGADO','SECRETARIA')")
    public ResponseEntity<List<String>> findStatusDocumento() {
        List<String> statusDocumento = documentoService.findStatusDocumento();
        return ResponseEntity.status(HttpStatus.OK).body(statusDocumento);
    }
}
