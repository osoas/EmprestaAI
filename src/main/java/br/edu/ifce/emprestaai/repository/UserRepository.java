package br.edu.ifce.emprestaai.repository;

import br.edu.ifce.emprestaai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndSenha(String email, String senha);


}
