package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Item;

import br.edu.ifce.emprestaai.repository.ItemRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/item")
public class ItemController {


    private final ItemRepository itemRepository;


    private ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    //TODO -> implementar paginamento


    @GetMapping("/list")
    private List<Item> getItens() {
        return itemRepository.findAll();
    }

    @GetMapping("/{id}")
    private Item getItem(@PathVariable Integer id) {
        return itemRepository.findById(id).orElse(null);
    }

    @PostMapping
    private Item postItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @PutMapping
    private Item putItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @DeleteMapping("/{id}")
    private void deleteItem(@PathVariable Integer id) {
        itemRepository.deleteById(id);
    }
}
