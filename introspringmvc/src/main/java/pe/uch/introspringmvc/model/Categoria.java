package pe.uch.introspringmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- AGREGA ESTO
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_categoria")
    private Integer idCategoria;
    
    @Column(nullable = false, unique = true) 
    private String nombre;
    
    // AGREGA EL @JsonIgnore AQUÍ
    @JsonIgnore
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY) 
    private List<Producto> productos;

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}