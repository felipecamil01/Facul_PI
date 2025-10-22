package com.Advocacia.DTO;

import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteOverviewDTO {
  private Cliente cliente;
  private List<ProcessoResumoDTO> processos;
  private List<Pagamento> pagamentos;
  private List<DocumentoResumoDTO> documentos;
  private List<AgendaResumoDTO> prazosImportantes;
}
