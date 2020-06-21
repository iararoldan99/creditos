package ar.com.ada.creditos.reportes;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ReporteCancelacion {
    /*
     * 3) Reportes sobre cancelaciones(usar los reportes que hayamos hecho) y
     * encapsular!
     */

    @Id
    private int cuotasPagadas; // por cada prestamo
    private int prestamosPagadosEnTotal; // en total

    public int getCuotasPagadas() {
        return cuotasPagadas;
    }

    public void setCuotasPagadas(int cuotasPagadas) {
        this.cuotasPagadas = cuotasPagadas;
    }

    public int getPrestamosPagadosEnTotal() {
        return prestamosPagadosEnTotal;
    }

    public void setPrestamosPagadosEnTotal(int prestamosPagadosEnTotal) {
        this.prestamosPagadosEnTotal = prestamosPagadosEnTotal;
    }

}