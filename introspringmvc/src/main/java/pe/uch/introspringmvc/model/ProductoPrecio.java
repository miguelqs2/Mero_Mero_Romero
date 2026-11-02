package pe.uch.introspringmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "productos_precios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductoPrecio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_precio")
    private Integer idPrecio;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false) private String tamano;
    @Column(nullable = false) private Double precio;
    @Column(nullable = false) private Integer stock = 50;
    @Column(nullable = false) private String estado = "DISPONIBLE";
    @Column(name = "tiempo_preparacion") private String tiempoPreparacion = "20 min";

    public Integer getIdPrecio() { return idPrecio; }
    public void setIdPrecio(Integer idPrecio) { this.idPrecio = idPrecio; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(String tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }
}