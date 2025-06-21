package com.Advocacia.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @PastOrPresent
    private LocalDate dataUltimoContato;

    @NotBlank
    private String meioContato;

    @NotBlank
    private String notasContato;

    private String proximoPassos;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente cliente;

}
