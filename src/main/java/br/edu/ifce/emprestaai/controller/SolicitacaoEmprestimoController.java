package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.*;

import br.edu.ifce.emprestaai.repository.EmprestimoRepository;
import br.edu.ifce.emprestaai.repository.PagamentoRepository;
import br.edu.ifce.emprestaai.repository.SolicitacaoEmprestimoRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/solicitacao")
public class SolicitacaoEmprestimoController {

    private final SolicitacaoEmprestimoRepository solicitacaoAvaliacaoRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final PagamentoRepository pagamentoRepository;

    public SolicitacaoEmprestimoController(
            SolicitacaoEmprestimoRepository solicitacaoAvaliacaoRepository,
            EmprestimoRepository avaliacaoRepository,
            PagamentoRepository pagamentoRepository
    ) {
        this.solicitacaoAvaliacaoRepository = solicitacaoAvaliacaoRepository;
        this.emprestimoRepository = avaliacaoRepository;
        this.pagamentoRepository = pagamentoRepository;
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
    public SolicitacaoEmprestimo mudarStatusSolicitacao(
            @RequestParam Integer id,
            @RequestParam StatusSolicitacao statusSolicitacao
    ) {
        SolicitacaoEmprestimo solicitacao = solicitacaoAvaliacaoRepository.findById(id).orElse(null);

        if (solicitacao == null) return null;

        solicitacao.setStatus(statusSolicitacao);

        if (statusSolicitacao == StatusSolicitacao.APROVADO) {

            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setItem(solicitacao.getItem());
            emprestimo.setDestinatario(solicitacao.getUsuario());
            emprestimo.setRemetente(solicitacao.getItem().getProprietario());
            emprestimo.setSolicitacaoEmprestimo(solicitacao);
            emprestimo.setData_devolucao_prevista(solicitacao.getData_fim());
            emprestimo.setData_inicio(solicitacao.getData_inicio());

            emprestimoRepository.save(emprestimo);

            Pagamento pagamento = new Pagamento();
            pagamento.setEmprestimo(emprestimo);
            pagamento.setStatusPagamento(StatusEmprestimo.PENDENTE);
            pagamento.setValor(emprestimo.getItem().getValor_unitario());
            pagamento.setUsuario(emprestimo.getDestinatario());
            pagamentoRepository.save(pagamento);

            emprestimo.setPagamento(pagamento);
            emprestimoRepository.save(emprestimo);
        }

        return solicitacaoAvaliacaoRepository.save(solicitacao);
    }

    @DeleteMapping("/{id}")
    public void deleteSolicitacao(@PathVariable Integer id) {
        solicitacaoAvaliacaoRepository.deleteById(id);
    }
}
