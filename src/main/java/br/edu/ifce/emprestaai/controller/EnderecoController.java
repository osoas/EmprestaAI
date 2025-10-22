package br.edu.ifce.emprestaai.controller;
import br.edu.ifce.emprestaai.model.Endereco;
import br.edu.ifce.emprestaai.repository.EnderecoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/Endereco")
public class EnderecoController {


    private final EnderecoRepository enderecoRepository;


    public EnderecoController(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @GetMapping("/list")
    private List<Endereco> getEnderecos() {
        return enderecoRepository.findAll();
    }

    @GetMapping("/{id}")
    private Endereco getEndereco(@PathVariable Integer id) {
        return enderecoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Endereco cadastrarEndereco(@RequestBody Endereco emprestimo) {
        return enderecoRepository.save(emprestimo);
    }

    @PutMapping
    private Endereco putEndereco(@RequestBody Endereco emprestimo) {
        return enderecoRepository.save(emprestimo);
    }

    @DeleteMapping("/{id}")
    private void deleteEndereco(@PathVariable Integer id) {
        enderecoRepository.deleteById(id);
    }
}
