package com.Advocacia.Entity;

import com.Advocacia.Auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import com.Advocacia.Entity.Parcela;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
public class Agenda extends AuditoriaEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime data;

    private String descricao;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "prazo_importante")
    private boolean prazoImportante;

    @Column(name = "prioridade")
    private String prioridade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;

    @ManyToOne
    @JoinColumn(name = "parcela_id")
    private Parcela parcela;
}
