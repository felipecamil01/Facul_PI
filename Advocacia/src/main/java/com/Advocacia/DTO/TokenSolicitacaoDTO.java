package com.Advocacia.DTO;

import com.Advocacia.Auth.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenSolicitacaoDTO {
  private String email;
  private UserRole role;
  private Integer validadeHoras;
}
