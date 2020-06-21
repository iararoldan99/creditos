package ar.com.ada.creditos.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// Entity indica que esa clase actuara como entidad
// Table indica a que tabla va a persistirse ese objeto
@Entity
@Table(name = "prestamo")

public class Prestamo {
    @Id // que es una Primary Key
    @Column(name = "prestamo_id") // nombre con el que se mapea a la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private int prestamoId;
    private BigDecimal importe; // se mapea automaticamente
    private Date fecha;
    @Column(name = "cuota")
    private int cuotas;
    @Column(name = "fecha_alta")
    private Date fechaAlta;
    @Column(name= "estado_id")
    private int estadoId;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;

    public int getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.cliente.getPrestamo().add(this);
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public EstadoPrestamoEnum getEstadoId() {

        return EstadoPrestamoEnum.parse(this.estadoId);
    }

    public void setEstadoId(EstadoPrestamoEnum estadoId) {
        this.estadoId = estadoId.getValue();
    }

    public enum EstadoPrestamoEnum {
        SOLICITADO(1), 
        RECHAZADO(2),
        PENDIENTE_APROBACION(3),
        APROBADO(4);

        private final int value;

        // NOTE: Enum constructor tiene que estar en privado
        private EstadoPrestamoEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static EstadoPrestamoEnum parse(int id) {
            EstadoPrestamoEnum status = null; // Default
            for (EstadoPrestamoEnum item : EstadoPrestamoEnum.values()) {
                if (item.getValue() == id) {
                    status = item;
                    break;
                }
            }
            return status;
        }
    }

}