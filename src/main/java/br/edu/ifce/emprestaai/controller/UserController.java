package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Endereco;
import br.edu.ifce.emprestaai.model.User;
import br.edu.ifce.emprestaai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {


    private final UserRepository userRepository;

    // Construtor para injeção de dependência
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @GetMapping("/list")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User usuario) {
        if (usuario.getEnderecos() != null) {
            for (Endereco endereco : usuario.getEnderecos()) {
                endereco.setUsuario(usuario);
            }
        }

        User savedUsuario = userRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

}
