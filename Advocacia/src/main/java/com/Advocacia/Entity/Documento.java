package com.Advocacia.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.Audited;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ElementCollection
    private List<String> listaDocumentos = new ArrayList<>();

    @PastOrPresent
    private LocalDate dataRecebimento;

    @NotBlank
    private String statusDocumento;

    private String observacao;

    @ManyToMany(mappedBy = "documentos")
    private List<Processo> processos = new ArrayList<>();
}
