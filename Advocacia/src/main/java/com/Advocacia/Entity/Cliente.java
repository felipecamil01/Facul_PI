package com.Advocacia.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @NotBlank
    private String nome;
    @CPF
    private String cpf;
    @NotBlank
    private String rg ;
    @Past
    private LocalDate dataNascimento;
    @NotBlank
    private EstadoCivil estadoCivil;
    @NotBlank
    private String profissao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="endereco_id",referencedColumnName = "id")
    private Endereco endereco;
    @ElementCollection
    private List<String> telefone;
    @Email
    private String email;
    @OneToMany(mappedBy = "cliente",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Processo>processos = new ArrayList<>();


}
