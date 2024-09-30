package com.Advocacia.Service;

import com.Advocacia.Entity.Documento;
import com.Advocacia.Entity.Endereco;
import com.Advocacia.Repository.DocumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Documento save(Documento documentos) {
        return documentoRepository.save(documentos);
    }

    public Documento update(Long id, Documento documentoAtualizada) {
        Optional<Documento> documentoExistente = documentoRepository.findById(id);

        if (documentoExistente.isPresent()) {
            documentoAtualizada.setId(id);
            return documentoRepository.save(documentoAtualizada);
        }else
        	throw new EntityNotFoundException("Documentação não encontrada");
    }

    public void delete(Long id) {
        if (documentoRepository.existsById(id)) {
            documentoRepository.deleteById(id);
        } else 
            throw new EntityNotFoundException("Documentação não encontrada");
    }

    public List<Documento> findAll() {
        return documentoRepository.findAll();
    }

    public Optional<Documento> findById(Long id) {
        return documentoRepository.findById(id);
    }
}
