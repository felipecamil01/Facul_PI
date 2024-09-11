package com.Advocacia.Service;

import com.Advocacia.Entity.Documentos;
import com.Advocacia.Repository.DocumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Documentos salvarDocumentacao(Documentos documentos) {
        return documentoRepository.save(documentos);
    }

    public Page<Documentos> listarTodaDocumentacao(Pageable pageable) {
        return documentoRepository.findAll(pageable);
    }

    public Optional<Documentos> buscarDocumentacaoPorId(Long id) {
        return documentoRepository.findById(id);
    }

    public Documentos atualizarDocumentacao(Long id, Documentos documentoAtualizada) {
        Optional<Documentos> documentoExistente = documentoRepository.findById(id);

        if (documentoExistente.isPresent()) {
            documentoAtualizada.setId(id);
            return documentoRepository.save(documentoAtualizada);
        }

        throw new EntityNotFoundException("Documentação não encontrada");
    }

    public void deletarDocumentacao(Long id) {
        if (documentoRepository.existsById(id)) {
            documentoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Documentação não encontrada");
        }

    }
}
