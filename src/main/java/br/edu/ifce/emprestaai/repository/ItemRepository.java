package br.edu.ifce.emprestaai.repository;

import br.edu.ifce.emprestaai.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByProprietarioId(Integer proprietarioId);
}
