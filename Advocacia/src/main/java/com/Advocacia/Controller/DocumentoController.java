package com.Advocacia.Controller;

import com.Advocacia.Entity.Documento;
import com.Advocacia.Service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/documento")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/save")
    public ResponseEntity<Documento> save(@RequestBody Documento documentoNovo) {
    	Documento documento = documentoService.save(documentoNovo);
        return ResponseEntity.status(HttpStatus.CREATED).body(documento);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Documento> update(@PathVariable Long id, @RequestBody Documento documentoAtualizado) {
    	Documento documento = documentoService.update(id, documentoAtualizado);
	    return ResponseEntity.status(HttpStatus.OK).body(documento);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	documentoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Documento>> findAll() {
        List<Documento> documentos = documentoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(documentos);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Documento> findById(@PathVariable Long id) {
    	Documento documento = documentoService.findById(id);
    	return ResponseEntity.status(HttpStatus.OK).body(documento);
    }
    
    @GetMapping("/findStatusDocumento")
    public ResponseEntity<List<String>> findStatusDocumento(){
        List<String> statusDocumento = documentoService.findStatusDocumento();
        return ResponseEntity.status(HttpStatus.OK).body(statusDocumento);
    }
}