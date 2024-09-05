package com.Advocacia.Service;

import com.Advocacia.Entity.Documentos;
import com.Advocacia.Repository.DocumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {
    @Autowired
    private DocumentoRepository documentoRepository;

    public Documentos salvarDocumentacao(Documentos documentos) {
        return documentoRepository.save(documentos);
    }

    public List<Documentos> listarTodaDocumentacao() {
        return documentoRepository.findAll();
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
