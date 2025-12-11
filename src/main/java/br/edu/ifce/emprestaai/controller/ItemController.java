package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.dto.ItemDTO;
import br.edu.ifce.emprestaai.model.Avaliacao;
import br.edu.ifce.emprestaai.model.Item;
import br.edu.ifce.emprestaai.repository.AvaliacaoRepository;
import br.edu.ifce.emprestaai.repository.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/item")
@CrossOrigin(origins = "*")
public class ItemController {


    private final ItemRepository itemRepository;
    private final AvaliacaoRepository avaliacaoRepository;


    private ItemController(ItemRepository itemRepository, AvaliacaoRepository avaliacaoRepository) {
        this.itemRepository = itemRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }





    @GetMapping("/list")
    public List<Item> getItens() {
        return itemRepository.findAll();
    }

    @GetMapping("/list/stats")
    public List<ItemDTO> getItensWithStats() {
        List<Item> items = itemRepository.findAll();
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAll();

        List<ItemDTO> dtos = new ArrayList<>();
        for (Item it : items) {
            ItemDTO dto = new ItemDTO();
            dto.setId(it.getId());
            dto.setNome_item(it.getNome_item());
            dto.setDescricao(it.getDescricao());
            dto.setValor_unitario(it.getValor_unitario());
            dto.setEstrelas(it.getEstrelas());
            dto.setCategoria_item(it.getCategoria_item());
            dto.setProprietarioId(it.getProprietario() != null ? it.getProprietario().getId() : null);

            double sum = 0.0;
            int count = 0;
            for (Avaliacao a : avaliacoes) {
                if (a.getEmprestimo() != null && a.getEmprestimo().getItem() != null && a.getEmprestimo().getItem().getId() != null
                        && a.getEmprestimo().getItem().getId().equals(it.getId())) {
                    if (a.getNota() != null) {
                        sum += a.getNota().doubleValue();
                        count++;
                    }
                }
            }
            if (count > 0) {
                dto.setRatingAvg(sum / count);
                dto.setRatingCount(count);
            } else {
                dto.setRatingAvg(null);
                dto.setRatingCount(0);
            }

            dtos.add(dto);
        }

        return dtos;
    }

    @GetMapping("/owner/{ownerId}")
    public List<Item> getByOwner(@PathVariable Integer ownerId) {
        return itemRepository.findByProprietarioId(ownerId);
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable Integer id) {
        return itemRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Item cadastrarItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @PutMapping
    public Item putItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemRepository.deleteById(id);
    }
}
