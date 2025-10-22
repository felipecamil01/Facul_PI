package com.Advocacia.Auth;

import lombok.*;

@ToString
public enum UserRole {
  ADMIN("ADMIN"),
  ADVOGADO("ADVOGADO"),
  SECRETARIA("SECRETARIA"),
  USER("USER");

  private final String role;

  UserRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}
