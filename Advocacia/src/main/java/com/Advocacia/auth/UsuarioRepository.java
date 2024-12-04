package com.Advocacia.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

  //Consulta username
  @Query("SELECT u FROM Usuario u WHERE u.username = :username")
  Optional<Usuario> findByUsername(@Param("username") String username);

  //Consulta  email
  Optional<Usuario> findByEmail(String email);

}
