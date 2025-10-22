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

    private final SolicitacaoEmprestimoRepository solicitacaoAvaliacaoRepository;
    private final EmprestimoRepository avaliacaoRepository;

    private SolicitacaoEmprestimoController(
            SolicitacaoEmprestimoRepository solicitacaoAvaliacaoRepository,
            EmprestimoRepository avaliacaoRepository
    ) {
        this.solicitacaoAvaliacaoRepository = solicitacaoAvaliacaoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }


    //TODO -> implementar paginamento


    @GetMapping("/list")
    public List<SolicitacaoEmprestimo> getSolicitacoes() {
        return solicitacaoAvaliacaoRepository.findAll();
    }

    //@GetMapping("/list/{id}")
    //public List<SolicitacaoEmprestimo> getSolicitacoesUser(@PathVariable Integer id) {
    //    return solicitacaoAvaliacaoRepository.findByUser(id);
    //}

    @GetMapping("/{id}")
    public SolicitacaoEmprestimo getSolicitacao(@PathVariable Integer id) {
        return solicitacaoAvaliacaoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public SolicitacaoEmprestimo psotSolicitacao(@RequestBody SolicitacaoEmprestimo solicitacaoAvaliacao) {
        return solicitacaoAvaliacaoRepository.save(solicitacaoAvaliacao);
    }

    @PutMapping
    public SolicitacaoEmprestimo putSolicitacao(@RequestBody SolicitacaoEmprestimo solicitacaoAvaliacao) {
        return solicitacaoAvaliacaoRepository.save(solicitacaoAvaliacao);
    }
    @PutMapping("/status")
    public SolicitacaoEmprestimo mudarStatusSolicitacao(@RequestParam Integer id, @RequestParam StatusSolicitacao statusSolicitacao) {
        SolicitacaoEmprestimo solicitacaoAvaliacao = solicitacaoAvaliacaoRepository.findById(id).orElse(null);
        if (solicitacaoAvaliacao != null) {
            solicitacaoAvaliacao.setStatus(statusSolicitacao);
            if (statusSolicitacao == StatusSolicitacao.APROVADO) {
                Emprestimo avaliacao = new Emprestimo();
                avaliacao.setItem(solicitacaoAvaliacao.getItem());
                avaliacao.setDestinatario(solicitacaoAvaliacao.getUsuario());
                avaliacao.setRemetente(solicitacaoAvaliacao.getItem().getProprietario());
                avaliacao.setSolicitacaoEmprestimo(solicitacaoAvaliacao);
                avaliacao.setData_devolucao_prevista(solicitacaoAvaliacao.getData_fim());
                avaliacao.setData_inicio(solicitacaoAvaliacao.getData_inicio());
                avaliacaoRepository.save(avaliacao);
            }
            return solicitacaoAvaliacaoRepository.save(solicitacaoAvaliacao);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteSolicitacao(@PathVariable Integer id) {
        solicitacaoAvaliacaoRepository.deleteById(id);
    }
}
