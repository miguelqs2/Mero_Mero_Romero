package pe.uch.introspringmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "items_pedido")
public class ItemPedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "id_precio", nullable = false)
    private ProductoPrecio productoPrecio;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "pedido_id", nullable = false) 
    @JsonIgnore
    private Pedido pedido;

    public ItemPedido() {}
    
    public ItemPedido(ProductoPrecio productoPrecio, Integer cantidad) {
        this.productoPrecio = productoPrecio;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductoPrecio getProductoPrecio() { return productoPrecio; }
    public void setProductoPrecio(ProductoPrecio productoPrecio) { this.productoPrecio = productoPrecio; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public double getSubtotal() { return productoPrecio.getPrecio() * cantidad; }
}