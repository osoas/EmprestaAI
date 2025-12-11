package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.dto.EmprestimoCreateDTO;
import br.edu.ifce.emprestaai.model.Emprestimo;
import br.edu.ifce.emprestaai.model.Item;
import br.edu.ifce.emprestaai.model.User;
import br.edu.ifce.emprestaai.model.Pagamento;
import br.edu.ifce.emprestaai.model.StatusEmprestimo;
import br.edu.ifce.emprestaai.repository.EmprestimoRepository;
import br.edu.ifce.emprestaai.repository.ItemRepository;
import br.edu.ifce.emprestaai.repository.UserRepository;
import br.edu.ifce.emprestaai.repository.PagamentoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("api/emprestimo")
@CrossOrigin(origins = "*")
public class EmprestimoApiController {

    private final EmprestimoRepository emprestimoRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(EmprestimoApiController.class);

    public EmprestimoApiController(EmprestimoRepository emprestimoRepository, ItemRepository itemRepository, UserRepository userRepository, PagamentoRepository pagamentoRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @GetMapping("/list")
    public List<Emprestimo> listAll() {
        return emprestimoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Emprestimo getById(@PathVariable Integer id) {
        return emprestimoRepository.findById(id).orElse(null);
    }

    @GetMapping("/user/{userId}")
    public List<Emprestimo> getByUser(@PathVariable Integer userId) {
        return emprestimoRepository.findBySolicitacaoEmprestimoUsuarioId(userId);
    }

    @GetMapping("/owner/{ownerId}")
    public List<Emprestimo> getByOwner(@PathVariable Integer ownerId) {
        return emprestimoRepository.findByItemProprietarioId(ownerId);
    }

    @PostMapping
    public Emprestimo create(@RequestBody JsonNode body) {
        logger.info("Creating Emprestimo from payload: {}", body == null ? "<null>" : body.toString());
        try {
            Emprestimo e = new Emprestimo();

            Integer itemId = null;
            Integer destinatarioId = null;
            String dataInicioStr = null;
            String dataDevolucaoStr = null;

            if (body == null || body.isNull()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty request body");
            }

            if (body.has("itemId") && body.get("itemId").canConvertToInt()) itemId = body.get("itemId").intValue();
            if (body.has("destinatarioId") && body.get("destinatarioId").canConvertToInt()) destinatarioId = body.get("destinatarioId").intValue();

            if (itemId == null && body.has("item") && body.get("item") != null) {
                JsonNode it = body.get("item");
                if (it.has("id") && it.get("id").canConvertToInt()) itemId = it.get("id").intValue();
                else if (it.has("id_item") && it.get("id_item").canConvertToInt()) itemId = it.get("id_item").intValue();
            }
            if (destinatarioId == null && body.has("destinatario") && body.get("destinatario") != null) {
                JsonNode d = body.get("destinatario");
                if (d.has("id") && d.get("id").canConvertToInt()) destinatarioId = d.get("id").intValue();
                else if (d.has("id_usuario") && d.get("id_usuario").canConvertToInt()) destinatarioId = d.get("id_usuario").intValue();
            }

            if (body.has("data_inicio") && !body.get("data_inicio").isNull()) dataInicioStr = body.get("data_inicio").asText();
            if (body.has("data_devolucao_prevista") && !body.get("data_devolucao_prevista").isNull()) dataDevolucaoStr = body.get("data_devolucao_prevista").asText();

            Item it = null;
            User dest = null;

            if (itemId != null) {
                it = itemRepository.findById(itemId).orElse(null);
                if (it == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not found");
                e.setItem(it);
                if (it.getProprietario() != null) {
                    e.setRemetente(it.getProprietario());
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "itemId is required");
            }

            if (destinatarioId != null) {
                dest = userRepository.findById(destinatarioId).orElse(null);
                if (dest == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destinatario not found");
                e.setDestinatario(dest);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "destinatarioId is required");
            }

            if (dataInicioStr != null && !dataInicioStr.isBlank()) {
                LocalDateTime dt = tryParseToLocalDateTime(dataInicioStr);
                if (dt != null) e.setData_inicio(dt);
            }
            if (dataDevolucaoStr != null && !dataDevolucaoStr.isBlank()) {
                LocalDateTime dt2 = tryParseToLocalDateTime(dataDevolucaoStr);
                if (dt2 != null) e.setData_devolucao_prevista(dt2);
            }

            Emprestimo saved = emprestimoRepository.save(e);
            logger.info("Emprestimo created with id {}", saved.getId());
            return saved;
        } catch (ResponseStatusException rse) {
            logger.warn("Bad request creating emprestimo: {}", rse.getReason());
            throw rse;
        } catch (Exception ex) {
            logger.error("Unexpected error creating emprestimo", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
        }
    }

    private LocalDateTime tryParseToLocalDateTime(String s) {
        if (s == null) return null;
        s = s.trim();
        try {
            if (s.endsWith("Z") || s.matches(".*[+-]\\d{2}:?\\d{2}$")) {
                Instant inst = Instant.parse(s);
                return LocalDateTime.ofInstant(inst, ZoneId.systemDefault());
            }
        } catch (DateTimeParseException ex) {
        }
        try {
            return LocalDateTime.parse(s);
        } catch (DateTimeParseException ex) {
            try {
                if (s.contains(".")) {
                    String truncated = s.substring(0, s.indexOf('.'));
                    return LocalDateTime.parse(truncated);
                }
            } catch (Exception e) {
            }
        }
        try {
            Instant inst = Instant.parse(s);
            return LocalDateTime.ofInstant(inst, ZoneId.systemDefault());
        } catch (Exception ex) {
            return null;
        }
    }

    @PutMapping("/status")
    public Emprestimo adminUpdateStatus(@RequestParam Integer id, @RequestParam String status) {
        Emprestimo e = emprestimoRepository.findById(id).orElse(null);
        if (e == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo not found");

        try {
            StatusEmprestimo st = StatusEmprestimo.valueOf(status);
            if (e.getPagamento() != null) {
                Pagamento p = e.getPagamento();
                p.setStatusPagamento(st);
                pagamentoRepository.save(p);
                e.setPagamento(p);
            } else {
                Pagamento p = new Pagamento();
                p.setEmprestimo(e);
                p.setStatusPagamento(st);
                p.setUsuario(e.getDestinatario());
                if (e.getItem() != null && e.getItem().getValor_unitario() != null) {
                    p.setValor(e.getItem().getValor_unitario());
                }
                pagamentoRepository.save(p);
                e.setPagamento(p);
            }
            return emprestimoRepository.save(e);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value");
        }
    }
}
