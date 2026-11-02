package pe.uch.introspringmvc.model;

public class ActividadConfeccion {

    private int orden;
    private String actividad;
    private String responsable;
    private String estado;
    private String tiempoEstimado;
    private String prioridad;
    private String documentoEvidencia;

    public ActividadConfeccion(int orden, String actividad, String responsable,
            String estado, String tiempoEstimado, String prioridad,
            String documentoEvidencia) {

        this.orden = orden;
        this.actividad = actividad;
        this.responsable = responsable;
        this.estado = estado;
        this.tiempoEstimado = tiempoEstimado;
        this.prioridad = prioridad;
        this.documentoEvidencia = documentoEvidencia;
    }

    public int getOrden() {
        return orden;
    }

    public String getActividad() {
        return actividad;
    }

    public String getResponsable() {
        return responsable;
    }

    public String getEstado() {
        return estado;
    }

    public String getTiempoEstimado() {
        return tiempoEstimado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getDocumentoEvidencia() {
        return documentoEvidencia;
    }
}