package ar.com.ada.creditos.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "cancelacion")

public class Cancelacion {
    /*
     * crear la entidad Cancelación y agregar: 1) Que se pueda agregar un pago a un
     * préstamo. 2) Que se pueda eliminar un pago a un préstamo(en forma LOGICA) 3)
     * Reportes sobre cancelaciones(usar los reportes que hayamos hecho) y
     * encapsular!
     */
    @Id // Refiere a la PRIMARY KEY
    @Column(name = "cancelacion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AutoIncremental
    private int cancelacionId;
    @ManyToOne
    @JoinColumn(name = "prestamo_id", referencedColumnName = "prestamo_id")
    private Prestamo prestamoId;
    @Column(name = "fecha_cancelacion")
    private Date fechaCancelacion;
    private BigDecimal importe;
    private int cuota;
    @Column(name= "estado_cancelacion")
    private int estadoId;

    // private List<Cliente> cliente;

    public Cancelacion(int cancelacionId, Prestamo prestamoId, Date fechaCancelacion, BigDecimal importe, int cuota) {
        this.cancelacionId = cancelacionId;
        this.prestamoId = prestamoId;
        this.fechaCancelacion = fechaCancelacion;
        this.importe = importe;
        this.cuota = cuota;

    }

    public Cancelacion() {
    }

    public int getCancelacionId() {
        return cancelacionId;
    }

    public void setCancelacionId(int cancelacionId) {
        this.cancelacionId = cancelacionId;
    }

    public Prestamo getPrestamoId() {
        return prestamoId;
    }

    public Date getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public int getCuota() {
        return cuota;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    public void setPrestamoId(Prestamo prestamoId) {
        this.prestamoId = prestamoId;
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