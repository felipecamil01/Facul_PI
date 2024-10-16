package com.Advocacia.Repository;

import com.Advocacia.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User,Long> {

   public  Optional<User> findByUser(String user);
}
