package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Avaliacao;
import br.edu.ifce.emprestaai.repository.AvaliacaoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/avaliacao")
public class AvaliacaoController {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoController(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @GetMapping("/list")
    public List<Avaliacao> getAvaliacoes() {
        return avaliacaoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Avaliacao getAvaliacao(@PathVariable Integer id) {
        return avaliacaoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Avaliacao cadastrarAvaliacao(@RequestBody Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    @PutMapping
    public Avaliacao putAvaliacao(@RequestBody Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    @DeleteMapping("/{id}")
    public void deleteAvaliacao(@PathVariable Integer id) {
        avaliacaoRepository.deleteById(id);
    }
}
