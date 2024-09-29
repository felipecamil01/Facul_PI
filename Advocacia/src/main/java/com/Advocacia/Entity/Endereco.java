package com.Advocacia.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Endereco {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  id;
    
    @NotBlank
    private String logradouro;
    
    @NotBlank
    private String numero;

    private String complemento;
    
    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;

    @NotBlank
    @Pattern(regexp = "\\d{5}-\\d{3}")
    private String cep;

}
