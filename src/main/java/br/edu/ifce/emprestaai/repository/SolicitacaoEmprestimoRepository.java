package br.edu.ifce.emprestaai.repository;

import br.edu.ifce.emprestaai.model.SolicitacaoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoEmprestimoRepository extends JpaRepository<SolicitacaoEmprestimo, Integer> {
    List<SolicitacaoEmprestimo> findByUser(Integer id);
}
