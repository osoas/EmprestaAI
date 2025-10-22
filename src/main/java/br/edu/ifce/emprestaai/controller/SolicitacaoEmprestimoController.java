package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Emprestimo;
import br.edu.ifce.emprestaai.model.SolicitacaoEmprestimo;

import br.edu.ifce.emprestaai.model.StatusSolicitacao;
import br.edu.ifce.emprestaai.repository.EmprestimoRepository;
import br.edu.ifce.emprestaai.repository.SolicitacaoEmprestimoRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/solicitacao")
public class SolicitacaoEmprestimoController {

    private final SolicitacaoEmprestimoRepository solicitacaoEmprestimoRepository;
    private final EmprestimoRepository emprestimoRepository;

    private SolicitacaoEmprestimoController(
            SolicitacaoEmprestimoRepository solicitacaoEmprestimoRepository,
            EmprestimoRepository emprestimoRepository
    ) {
        this.solicitacaoEmprestimoRepository = solicitacaoEmprestimoRepository;
        this.emprestimoRepository = emprestimoRepository;
    }


    //TODO -> implementar paginamento


    @GetMapping("/list")
    public List<SolicitacaoEmprestimo> getSolicitacoes() {
        return solicitacaoEmprestimoRepository.findAll();
    }

    @GetMapping("/list/{id}")
    public List<SolicitacaoEmprestimo> getSolicitacoesUser(@PathVariable Integer id) {
        return solicitacaoEmprestimoRepository.findByUser(id);
    }

    @GetMapping("/{id}")
    public SolicitacaoEmprestimo getSolicitacao(@PathVariable Integer id) {
        return solicitacaoEmprestimoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public SolicitacaoEmprestimo psotSolicitacao(@RequestBody SolicitacaoEmprestimo solicitacaoEmprestimo) {
        return solicitacaoEmprestimoRepository.save(solicitacaoEmprestimo);
    }

    @PutMapping
    public SolicitacaoEmprestimo putSolicitacao(@RequestBody SolicitacaoEmprestimo solicitacaoEmprestimo) {
        return solicitacaoEmprestimoRepository.save(solicitacaoEmprestimo);
    }
    @PutMapping("/status")
    public SolicitacaoEmprestimo mudarStatusSolicitacao(@RequestParam Integer id, @RequestParam StatusSolicitacao statusSolicitacao) {
        SolicitacaoEmprestimo solicitacaoEmprestimo = solicitacaoEmprestimoRepository.findById(id).orElse(null);
        if (solicitacaoEmprestimo != null) {
            solicitacaoEmprestimo.setStatus(statusSolicitacao);
            if (statusSolicitacao == StatusSolicitacao.APROVADO) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setItem(solicitacaoEmprestimo.getItem());
                emprestimo.setDestinatario(solicitacaoEmprestimo.getUsuario());
                emprestimo.setRemetente(solicitacaoEmprestimo.getItem().getProprietario());
                emprestimo.setSolicitacaoEmprestimo(solicitacaoEmprestimo);
                emprestimo.setData_devolucao_prevista(solicitacaoEmprestimo.getData_fim());
                emprestimo.setData_inicio(solicitacaoEmprestimo.getData_inicio());
                emprestimoRepository.save(emprestimo);
            }
            return solicitacaoEmprestimoRepository.save(solicitacaoEmprestimo);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteSolicitacao(@PathVariable Integer id) {
        solicitacaoEmprestimoRepository.deleteById(id);
    }
}
