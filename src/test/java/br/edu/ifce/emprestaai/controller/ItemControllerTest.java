package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Item;
import br.edu.ifce.emprestaai.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1);
        item.setNome_item("Item Teste");
        item.setDescricao("Descrição do Item Teste");
        item.setValor_unitario(BigDecimal.valueOf(100.00));
    }

    @Test
    void getItens_DeveRetornarListaDeItens() {
        // Arrange
        List<Item> itens = Arrays.asList(item);
        when(itemRepository.findAll()).thenReturn(itens);

        // Act
        List<Item> resultado = itemController.getItens();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(item.getNome_item(), resultado.get(0).getNome_item());
        verify(itemRepository).findAll();
    }

    @Test
    void getItem_QuandoExiste_DeveRetornarItem() {
        // Arrange
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        // Act
        Item resultado = itemController.getItem(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(item.getId(), resultado.getId());
        assertEquals(item.getNome_item(), resultado.getNome_item());
        verify(itemRepository).findById(1);
    }

    @Test
    void getItem_QuandoNaoExiste_DeveRetornarNull() {
        // Arrange
        when(itemRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        Item resultado = itemController.getItem(99);

        // Assert
        assertNull(resultado);
        verify(itemRepository).findById(99);
    }

    @Test
    void cadastrarItem_DeveSalvarNovoItem() {
        // Arrange
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // Act
        Item resultado = itemController.cadastrarItem(item);

        // Assert
        assertNotNull(resultado);
        assertEquals(item.getNome_item(), resultado.getNome_item());
        assertEquals(item.getValor_unitario(), resultado.getValor_unitario());
        verify(itemRepository).save(item);
    }

    @Test
    void putItem_DeveAtualizarItem() {
        // Arrange
        Item itemAtualizado = new Item();
        itemAtualizado.setId(1);
        itemAtualizado.setNome_item("Item Atualizado");
        itemAtualizado.setValor_unitario(BigDecimal.valueOf(150.00));
        when(itemRepository.save(any(Item.class))).thenReturn(itemAtualizado);

        // Act
        Item resultado = itemController.putItem(itemAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Item Atualizado", resultado.getNome_item());
        assertEquals(150.0, resultado.getValor_unitario());
        verify(itemRepository).save(itemAtualizado);
    }

    @Test
    void deleteItem_DeveDeletarItem() {
        // Arrange
        Integer id = 1;
        doNothing().when(itemRepository).deleteById(id);

        // Act
        itemController.deleteItem(id);

        // Assert
        verify(itemRepository).deleteById(id);
    }
}
