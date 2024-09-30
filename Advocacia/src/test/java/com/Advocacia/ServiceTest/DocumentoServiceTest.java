package com.Advocacia.ServiceTest;

import com.Advocacia.Entity.Documento;
import com.Advocacia.Repository.DocumentoRepository;
import com.Advocacia.Service.DocumentoService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class DocumentoServiceTest {

    @InjectMocks
    private DocumentoService documentoService;

    @Mock
    private DocumentoRepository documentoRepository;

    private Documento documento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        documento = new Documento();
        documento.setId(1L);
        documento.setListaDocumentos(Collections.singletonList("Documento A"));
        documento.setDataRecebimento(LocalDate.now());
        documento.setStatusDocumento("Recebido");
        documento.setObservacao("Documento importante");
    }

    @Test
    void testSaveDocumento() {
        Mockito.when(documentoRepository.save(any(Documento.class))).thenReturn(documento);

        Documento savedDocumento = documentoService.save(documento);

        assertNotNull(savedDocumento);
        assertEquals(1L, savedDocumento.getId());
        assertEquals("Recebido", savedDocumento.getStatusDocumento());
    }

    @Test
    void testUpdateDocumento() {
        Mockito.when(documentoRepository.findById(anyLong())).thenReturn(Optional.of(documento));
        Mockito.when(documentoRepository.save(any(Documento.class))).thenReturn(documento);

        Documento updatedDocumento = documentoService.update(1L, documento);

        assertNotNull(updatedDocumento);
        assertEquals(1L, updatedDocumento.getId());
    }

    @Test
    void testUpdateDocumentoNotFound() {
        Mockito.when(documentoRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            documentoService.update(1L, documento);
        });

        assertEquals("Documentação não encontrada", exception.getMessage());
    }

    @Test
    void testDeleteDocumento() {
        Mockito.when(documentoRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> documentoService.delete(1L));

        Mockito.verify(documentoRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDocumentoNotFound() {
        Mockito.when(documentoRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            documentoService.delete(1L);
        });

        assertEquals("Documentação não encontrada", exception.getMessage());
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Documento> documentoPage = new PageImpl<>(Collections.singletonList(documento));
        Mockito.when(documentoRepository.findAll(any(Pageable.class))).thenReturn(documentoPage);

        Page<Documento> result = documentoService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindById() {
        Mockito.when(documentoRepository.findById(anyLong())).thenReturn(Optional.of(documento));

        Optional<Documento> foundDocumento = documentoService.findById(1L);

        assertTrue(foundDocumento.isPresent());
        assertEquals(1L, foundDocumento.get().getId());
    }
}
