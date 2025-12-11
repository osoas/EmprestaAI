package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.User;
import br.edu.ifce.emprestaai.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public User login(@RequestBody User credentials) {
        if (credentials == null || credentials.getEmail() == null || credentials.getSenha() == null) {
            return null;
        }
        return userRepository.findByEmailAndSenha(credentials.getEmail(), credentials.getSenha());
    }
}
