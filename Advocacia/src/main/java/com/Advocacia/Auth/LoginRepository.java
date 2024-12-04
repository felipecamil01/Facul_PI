package com.Advocacia.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LoginRepository extends JpaRepository<Usuario, Long> {

  //Consulta username
  @Query("SELECT u FROM Usuario u WHERE u.username = :username")
  Optional<Usuario> findByUsername(@Param("username") String username);

  //Consulta  email
  @Query("SELECT u FROM Usuario u WHERE u.email = :email")
  Optional<Usuario> findByEmail(@Param("email") String email);

}
