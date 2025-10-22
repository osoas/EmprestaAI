package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Emprestimo;

import br.edu.ifce.emprestaai.repository.EmprestimoRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/Emprestimo")
public class EmprestimoController {


    public final EmprestimoRepository emprestimoRepository;


    public EmprestimoController(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }





    @GetMapping("/list")
    public List<Emprestimo> getItens() {
        return emprestimoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Emprestimo getEmprestimo(@PathVariable Integer id) {
        return emprestimoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Emprestimo cadastrarEmprestimo(@RequestBody Emprestimo emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }

    @PutMapping
    public Emprestimo putEmprestimo(@RequestBody Emprestimo emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }

    @DeleteMapping("/{id}")
    public void deleteEmprestimo(@PathVariable Integer id) {
        emprestimoRepository.deleteById(id);
    }
}
