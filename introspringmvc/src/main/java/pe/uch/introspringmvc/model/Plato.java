package pe.uch.introspringmvc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "platos")
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_plato", nullable = false, length = 150)
    private String nombrePlato;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(name = "tiempo_preparacion", length = 50)
    private String tiempoPreparacion;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false)
    private Integer stock;

    public Plato() {}

    public Plato(String nombrePlato, String tipo, String descripcion, Double precio,
                 String tiempoPreparacion, String estado, Integer stock) {
        this.nombrePlato = nombrePlato;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tiempoPreparacion = tiempoPreparacion;
        this.estado = estado;
        this.stock = stock;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombrePlato() { return nombrePlato; }
    public void setNombrePlato(String nombrePlato) { this.nombrePlato = nombrePlato; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public String getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(String tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}