package pe.uch.introspringmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.uch.introspringmvc.model.Pedido;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstadoInOrderByIdPedidoAsc(List<String> estados);
    List<Pedido> findByNombreMeseraOrderByIdPedidoDesc(String nombreMesera);
    List<Pedido> findByEstadoAndFechaHoraRealizadoBetween(String estado, LocalDateTime desde, LocalDateTime hasta);
    List<Pedido> findByNumeroMesaAndEstadoIn(Integer numeroMesa, List<String> estados);
}