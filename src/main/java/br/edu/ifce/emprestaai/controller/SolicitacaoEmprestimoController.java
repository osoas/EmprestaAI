package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.*;

import br.edu.ifce.emprestaai.repository.EmprestimoRepository;
import br.edu.ifce.emprestaai.repository.PagamentoRepository;
import br.edu.ifce.emprestaai.repository.SolicitacaoEmprestimoRepository;
import br.edu.ifce.emprestaai.repository.ItemRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/solicitacao")
public class SolicitacaoEmprestimoController {

    private final SolicitacaoEmprestimoRepository solicitacaoAvaliacaoRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ItemRepository itemRepository;

    public SolicitacaoEmprestimoController(
            SolicitacaoEmprestimoRepository solicitacaoAvaliacaoRepository,
            EmprestimoRepository avaliacaoRepository,
            PagamentoRepository pagamentoRepository,
            ItemRepository itemRepository
    ) {
        this.solicitacaoAvaliacaoRepository = solicitacaoAvaliacaoRepository;
        this.emprestimoRepository = avaliacaoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.itemRepository = itemRepository;
    }


    @GetMapping("/list")
    public List<SolicitacaoEmprestimo> getSolicitacoes() {
        return solicitacaoAvaliacaoRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<SolicitacaoEmprestimo> getSolicitacoesPorUsuario(@PathVariable Integer userId) {
        return solicitacaoAvaliacaoRepository.findByUsuarioId(userId);
    }

    @GetMapping("/owner/{ownerId}")
    public List<SolicitacaoEmprestimo> getSolicitacoesPorOwner(@PathVariable Integer ownerId) {
        return solicitacaoAvaliacaoRepository.findByItemProprietarioId(ownerId);
    }

    @GetMapping("/{id}")
    public SolicitacaoEmprestimo getSolicitacao(@PathVariable Integer id) {
        return solicitacaoAvaliacaoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public SolicitacaoEmprestimo psotSolicitacao(@RequestBody SolicitacaoEmprestimo solicitacao) {
        if (solicitacao.getData_solicitacao() == null) solicitacao.setData_solicitacao(LocalDateTime.now());
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        return solicitacaoAvaliacaoRepository.save(solicitacao);
    }

    @PutMapping
    public SolicitacaoEmprestimo putSolicitacao(@RequestBody SolicitacaoEmprestimo solicitacaoAvaliacao) {
        return solicitacaoAvaliacaoRepository.save(solicitacaoAvaliacao);
    }
    @PutMapping("/status")
    public ResponseEntity<Map<String, Object>> mudarStatusSolicitacao(
            @RequestParam Integer id,
            @RequestParam StatusSolicitacao statusSolicitacao
    ) {
        SolicitacaoEmprestimo solicitacao = solicitacaoAvaliacaoRepository.findById(id).orElse(null);

        Map<String, Object> result = new HashMap<>();

        if (solicitacao == null) return ResponseEntity.notFound().build();

        solicitacao.setStatus(statusSolicitacao);

        Emprestimo createdEmprestimo = null;

        if (statusSolicitacao == StatusSolicitacao.APROVADO) {

            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setItem(solicitacao.getItem());
            emprestimo.setDestinatario(solicitacao.getUsuario());
            emprestimo.setRemetente(solicitacao.getItem().getProprietario());
            emprestimo.setSolicitacaoEmprestimo(solicitacao);
            emprestimo.setData_devolucao_prevista(solicitacao.getData_fim());
            emprestimo.setData_inicio(solicitacao.getData_inicio());

            createdEmprestimo = emprestimoRepository.save(emprestimo);

            Pagamento pagamento = new Pagamento();
            pagamento.setEmprestimo(createdEmprestimo);
            pagamento.setStatusPagamento(StatusEmprestimo.PENDENTE);
            long days = 1;
            try {
                if (solicitacao.getData_inicio() != null && solicitacao.getData_fim() != null) {
                    Duration d = Duration.between(solicitacao.getData_inicio(), solicitacao.getData_fim());
                    days = Math.max(1, d.toDays());
                }
            } catch (Exception e) {
                days = 1;
            }
            pagamento.setValor(createdEmprestimo.getItem().getValor_unitario().multiply(java.math.BigDecimal.valueOf(days)));
            pagamento.setUsuario(createdEmprestimo.getDestinatario());
            pagamentoRepository.save(pagamento);

            createdEmprestimo.setPagamento(pagamento);
            emprestimoRepository.save(createdEmprestimo);
        }

        SolicitacaoEmprestimo savedSolicitacao = solicitacaoAvaliacaoRepository.save(solicitacao);

        result.put("solicitacao", savedSolicitacao);
        if (createdEmprestimo != null) result.put("emprestimo", createdEmprestimo);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public void deleteSolicitacao(@PathVariable Integer id) {
        solicitacaoAvaliacaoRepository.deleteById(id);
    }
}
