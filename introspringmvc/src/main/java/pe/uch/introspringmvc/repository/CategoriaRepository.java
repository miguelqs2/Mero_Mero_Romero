package pe.uch.introspringmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.uch.introspringmvc.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}