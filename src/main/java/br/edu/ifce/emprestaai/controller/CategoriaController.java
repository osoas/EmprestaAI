package br.edu.ifce.emprestaai.controller;
import br.edu.ifce.emprestaai.model.Categoria;
import br.edu.ifce.emprestaai.repository.CategoriaRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/categoria")
public class CategoriaController {


    private final CategoriaRepository categoriaRepository;


    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/list")
    private List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    private Categoria getEndereco(@PathVariable Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Categoria cadastrarCategoria(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @PutMapping
    private Categoria putCategoria(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @DeleteMapping("/{id}")
    private void deleteCategoria(@PathVariable Integer id) {
        categoriaRepository.deleteById(id);
    }
}
