package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.SolicitacaoEmprestimo;

import br.edu.ifce.emprestaai.repository.SolicitacaoEmprestimoRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/solicitacao")
public class SolicitacaoEmprestimoController {


    private final SolicitacaoEmprestimoRepository solicitacaoEmprestimoRepository;


    private SolicitacaoEmprestimoController(SolicitacaoEmprestimoRepository solicitacaoEmprestimoRepository) {
        this.solicitacaoEmprestimoRepository = solicitacaoEmprestimoRepository;
    }


    //TODO -> implementar paginamento


    @GetMapping("/list")
    private List<SolicitacaoEmprestimo> getSolicitacoes() {
        return solicitacaoEmprestimoRepository.findAll();
    }

    @GetMapping("/list/{id}")
    private List<SolicitacaoEmprestimo> getSolicitacoesUser(@PathVariable Integer id) {
        return solicitacaoEmprestimoRepository.findByUser(id);
    }

    @GetMapping("/{id}")
    private SolicitacaoEmprestimo getSolicitacao(@PathVariable Integer id) {
        return solicitacaoEmprestimoRepository.findById(id).orElse(null);
    }

    @PostMapping
    private SolicitacaoEmprestimo psotSolicitacao(@RequestBody SolicitacaoEmprestimo solicitacaoEmprestimo) {
        return solicitacaoEmprestimoRepository.save(solicitacaoEmprestimo);
    }

    @PutMapping
    private SolicitacaoEmprestimo putSolicitacao(@RequestBody SolicitacaoEmprestimo solicitacaoEmprestimo) {
        return solicitacaoEmprestimoRepository.save(solicitacaoEmprestimo);
    }

    @DeleteMapping("/{id}")
    private void deleteSolicitacao(@PathVariable Integer id) {
        solicitacaoEmprestimoRepository.deleteById(id);
    }
}
