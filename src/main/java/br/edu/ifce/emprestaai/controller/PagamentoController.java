package br.edu.ifce.emprestaai.controller;
import br.edu.ifce.emprestaai.model.Pagamento;
import br.edu.ifce.emprestaai.repository.PagamentoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/pagamento")
public class PagamentoController {


    public final PagamentoRepository pagamentoRepository;


    public PagamentoController(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @GetMapping("/list")
    public List<Pagamento> getPagamentos() {
        return pagamentoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pagamento getPagamento(@PathVariable Integer id) {
        return pagamentoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Pagamento criarPagamento(@RequestBody Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    @PutMapping
    public Pagamento putPagamento(@RequestBody Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    @DeleteMapping("/{id}")
    public void deletePagamento(@PathVariable Integer id) {
        pagamentoRepository.deleteById(id);
    }
}
