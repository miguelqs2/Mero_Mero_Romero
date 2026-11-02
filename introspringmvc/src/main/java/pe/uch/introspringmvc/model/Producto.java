package pe.uch.introspringmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- AGREGA ESTO
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_producto")
    private Integer idProducto;
    
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
    
    @Column(nullable = false) 
    private String nombre;
    
    @Column(columnDefinition = "TEXT") 
    private String descripcion;
    
    // AGREGA EL @JsonIgnore AQUÍ
    @JsonIgnore
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
    private List<ProductoPrecio> precios;

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<ProductoPrecio> getPrecios() { return precios; }
    public void setPrecios(List<ProductoPrecio> precios) { this.precios = precios; }
}