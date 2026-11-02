package pe.uch.introspringmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_pedido")
    private Long idPedido;

    @Column(name = "numero_mesa", nullable = false)
    private Integer numeroMesa;

    @Column(name = "nombre_mesera", length = 50)
    private String nombreMesera;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"pedido", "hibernateLazyInitializer", "handler"})
    private List<ItemPedido> items = new ArrayList<>();

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_hora_pedido")
    private LocalDateTime fechaHoraPedido;

    @Column(name = "fecha_hora_realizado")
    private LocalDateTime fechaHoraRealizado;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Transient
    private List<String> listaNotasSeparadas;

    public Pedido() {}

    public Pedido(Integer numeroMesa, String nombreMesera, Double total) {
        this.numeroMesa = numeroMesa;
        this.nombreMesera = nombreMesera;
        this.total = total;
        this.estado = "PENDIENTE";
        this.fechaHoraPedido = LocalDateTime.now();
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Integer getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(Integer numeroMesa) { this.numeroMesa = numeroMesa; }
    public String getNombreMesera() { return nombreMesera; }
    public void setNombreMesera(String nombreMesera) { this.nombreMesera = nombreMesera; }
    public List<ItemPedido> getItems() { return items; }
    public void setItems(List<ItemPedido> items) { this.items = items; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public String getEstado() { return estado; }
    
    public void setEstado(String estado) {
        this.estado = estado;
        if ("REALIZADO".equals(estado) && fechaHoraRealizado == null) {
            this.fechaHoraRealizado = LocalDateTime.now();
        }
    }

    public LocalDateTime getFechaHoraPedido() { return fechaHoraPedido; }
    public void setFechaHoraPedido(LocalDateTime fechaHoraPedido) { this.fechaHoraPedido = fechaHoraPedido; }
    public LocalDateTime getFechaHoraRealizado() { return fechaHoraRealizado; }
    public void setFechaHoraRealizado(LocalDateTime fechaHoraRealizado) { this.fechaHoraRealizado = fechaHoraRealizado; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<String> getListaNotasSeparadas() { return listaNotasSeparadas; }
    public void setListaNotasSeparadas(List<String> listaNotasSeparadas) { this.listaNotasSeparadas = listaNotasSeparadas; }

    public String getDuracionPreparacion() {
        if (fechaHoraRealizado == null || fechaHoraPedido == null) return "En preparación";
        long minutos = Math.max(0, Duration.between(fechaHoraPedido, fechaHoraRealizado).toMinutes());
        return minutos + (minutos == 1 ? " minuto" : " minutos");
    }
}