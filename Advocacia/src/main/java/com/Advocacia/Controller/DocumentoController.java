package com.Advocacia.Controller;

import com.Advocacia.Entity.Documento;
import com.Advocacia.Service.DocumentoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documento")
@CrossOrigin("*")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    private final String uploadDir = "C:\\Users\\soare\\Documents\\4PeriodoFacul\\Nova pasta\\Facul_PI\\uploads\\";

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> handleFileUpload(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("dataRecebimento") LocalDate dataRecebimento,
            @RequestParam("statusDocumento") String statusDocumento,
            @RequestParam("observacao") String observacao) {

        Documento documento = new Documento();
        documento.setDataRecebimento(dataRecebimento);
        documento.setStatusDocumento(statusDocumento);
        documento.setObservacao(observacao);

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Por favor, selecione um arquivo para enviar."));
            }

            try {
                // Salvar o arquivo
                String filePath = uploadDir + file.getOriginalFilename();
                file.transferTo(new File(filePath));

                // Adicionar o caminho do arquivo à lista de documentos
                documento.getListaDocumentos().add(filePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Falha ao enviar o arquivo: " + e.getMessage()));
            }
        }

        try {
            // Tenta salvar o documento
            documentoService.save(documento);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Documento não encontrado"));
        }

        // Retornar uma resposta em formato JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Arquivos enviados com sucesso!");
        return ResponseEntity.ok(response);
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
    public ResponseEntity<List<Documento>> findAll() {
        List<Documento> documentacoes = documentoService.findAll();
        return ResponseEntity.ok(documentacoes);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Documento> findById(@PathVariable Long id) {
        return documentoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
