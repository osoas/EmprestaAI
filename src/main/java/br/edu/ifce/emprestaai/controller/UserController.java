package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Endereco;
import br.edu.ifce.emprestaai.model.User;
import br.edu.ifce.emprestaai.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserRepository userRepository;
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
    public ResponseEntity<User> cadastrarUser(@RequestBody User usuario) {
        if (usuario.getEnderecos() != null) {
            for (Endereco endereco : usuario.getEnderecos()) {
                endereco.setUsuario(usuario);
            }
        }

        User savedUsuario = userRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    @GetMapping("/login")
    public User loginUser(@RequestParam String email, @RequestParam String password) {
        return userRepository.findByEmailAndSenha(email, password);
    }

    @PatchMapping("/{id}/rate")
    public ResponseEntity<User> rateUser(@PathVariable Integer id, @RequestParam Double rate) {
        return userRepository.findById(id).map(user -> {
            java.math.BigDecimal currentStars = user.getEstrelas();
            int total = user.getTotalAvaliacoes() == null ? 0 : user.getTotalAvaliacoes();

            // (atual * total + nova) / (total + 1)
            double currentVal = currentStars.doubleValue();
            double newVal = (currentVal * total + rate) / (total + 1);

            user.setEstrelas(java.math.BigDecimal.valueOf(newVal));
            user.setTotalAvaliacoes(total + 1);
            
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }).orElse(ResponseEntity.notFound().build());
    }
}
