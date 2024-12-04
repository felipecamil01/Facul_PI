package com.Advocacia.ServiceTest;

import com.Advocacia.Entity.Processo;
import com.Advocacia.Repository.ProcessoRepository;
import com.Advocacia.Service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class ProcessoServiceTest {

    @InjectMocks
    private ProcessoService processoService;

    @Mock
    private ProcessoRepository processoRepository;

    private Processo processo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        processo = new Processo();
        processo.setId(1L);
        processo.setTipoCliente("Físico");
        processo.setAreaAtuacao("Direito Civil");
        processo.setNumeroProcesso("0001234-56.2024.8.00.0000");
        processo.setComarca("São Paulo");
        processo.setDataInicio(LocalDate.now());
        processo.setDescricao("Descrição do processo.");
        processo.setAndamento("Em andamento");
        processo.setSituacaoAtual("Aguardando sentença");
        processo.setPrazosImportantes(Collections.singletonList(LocalDate.now().plusDays(10)));
    }

    @Test
    void testSaveProcesso() {
        Mockito.when(processoRepository.save(any(Processo.class))).thenReturn(processo);

        Processo savedProcesso = processoService.save(processo);

        assertNotNull(savedProcesso);
        assertEquals(1L, savedProcesso.getId());
        assertEquals("0001234-56.2024.8.00.0000", savedProcesso.getNumeroProcesso());
    }

    @Test
    void testUpdateProcesso() {
        Mockito.when(processoRepository.findById(anyLong())).thenReturn(Optional.of(processo));
        Mockito.when(processoRepository.save(any(Processo.class))).thenReturn(processo);

        Processo updatedProcesso = processoService.update(1L, processo);

        assertNotNull(updatedProcesso);
        assertEquals(1L, updatedProcesso.getId());
    }

    @Test
    void testUpdateProcessoNotFound() {
        Mockito.when(processoRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            processoService.update(1L, processo);
        });

        assertEquals("Processo não encontrado", exception.getMessage());
    }

    @Test
    void testDeleteProcesso() {
        Mockito.when(processoRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> processoService.delete(1L));

        Mockito.verify(processoRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProcessoNotFound() {
        Mockito.when(processoRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            processoService.delete(1L);
        });

        assertEquals("Processo não encontrado", exception.getMessage());
    }

    @Test
    void testFindAll() {
        List<Processo> processoList = Collections.singletonList(processo);
        Mockito.when(processoRepository.findAll()).thenReturn(processoList);

        List<Processo> result = processoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Mockito.when(processoRepository.findById(anyLong())).thenReturn(Optional.of(processo));

        Optional<Processo> foundProcesso = processoService.findById(1L);

        assertTrue(foundProcesso.isPresent());
        assertEquals(1L, foundProcesso.get().getId());
    }

    @Test
    void testFindByNumeroProcesso() {
        Mockito.when(processoRepository.findByNumeroProcesso(any(String.class)))
                .thenReturn(Collections.singletonList(processo));

        List<Processo> foundProcessos = processoService.findByNumeroProcesso("0001234-56.2024.8.00.0000");

        assertNotNull(foundProcessos);
        assertEquals(1, foundProcessos.size());
        assertEquals("0001234-56.2024.8.00.0000", foundProcessos.get(0).getNumeroProcesso());
    }
}
