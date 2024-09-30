package com.Advocacia.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    
    @NotBlank(message = "Campo Nome não pode estar vazio")
    private String nome;
    
    @NotBlank(message = "Campo E-mail não pode estar vazio")
    @Email(message = "E-mail é inválido")
    private String email;
    
    @NotBlank(message = "Campo Cpf não pode estar vazio")
    @CPF(message = "CPF é inválido")
    private String cpf;
    
    @NotBlank(message = "Campo RG não pode estar vazio")
    @Pattern(regexp = "^\\d{1,2}\\.\\d{3}\\.\\d{3}-\\d{1}$", 
    message = "RG inválido. O formato deve ser XX.XXX.XXX-X.")
    private String rg;
    
    @NotBlank(message = "Campo Profissão não pode estar vazio")
    private String profissao;
    
    @NotBlank(message = "Campo telefone não pode estar vazio")
    @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$", 
    message = "Telefone inválido. O formato deve ser (XX)XXXXX-XXXX.")
    private String telefone;
    
    @Past(message = "Campo data de nascimento deve ser no passado")
    @NotNull(message = "Campo data de nascimento não pode estar vazio")
    private LocalDate dataNascimento;
    
    @NotNull(message = "Campo Estado Civil não pode estar vazio")
    @Enumerated(EnumType.STRING)
    private StatusCivil estadoCivil;
    
    @NotNull(message = "Campo Status Cliente não pode estar vazio")
    @Enumerated(EnumType.STRING)
    private StatusCliente statusCliente;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="endereco_id",referencedColumnName = "id")
    private Endereco endereco;
    
    @OneToMany(mappedBy = "cliente",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Processo>processos = new ArrayList<>();

}
