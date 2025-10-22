package com.Advocacia.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroComTokenDTO {
  private String username;
  private String email;
  private String password;
  private String token;
}
