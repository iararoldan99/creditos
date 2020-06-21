package ar.com.ada.creditos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import ar.com.ada.creditos.ABM;
import ar.com.ada.creditos.entities.Cliente;
import ar.com.ada.creditos.entities.Prestamo;
import ar.com.ada.creditos.managers.ClienteManager;
import ar.com.ada.creditos.managers.PrestamoManager;
import ar.com.ada.creditos.reportes.ReporteCliente;
import ar.com.ada.creditos.reportes.ReportePrestamo;

/*
Hacer una seccion Reportes con los siguientes reportes:
1) por cliente, saber cuantos prestamos tiene, cual el importe mas alto y cuanto en total de prestamos.
2) obtenemos la cantidad de prestamos y cuanta plata en total es.
Después de eso: ahí si, crear la entidad Cancelación y agregar:
1) Que se pueda agregar un pago a un préstamo.
2) Que se pueda eliminar un pago a un préstamo(en forma LOGICA)
3) Reportes sobre cancelaciones(usar los reportes que hayamos hecho)
y encapsular!
*/
public class ReporteService {
    public static Scanner Teclado = new Scanner(System.in);
    PrestamoManager pm;

    public void mostrarReporteCliente(int clienteID) {

        List<ReporteCliente> lista = pm.reporteCliente(clienteID);
        for (ReporteCliente rc : lista) {
            System.out.print("Este es el reporte del cliente solicitado. Su Id es: " + rc.getClienteId() + " Nombre: "
                    + rc.getNombre() + "Cantidad de prestamos: " + rc.getCantidad() + " Prestamo de maximo importe: "
                    + rc.getMaximo() + " Total entre todos los prestamos: " + rc.getTotal());

        }

    }

    public void mostrarReportePrestamoTotal() {

        List<ReportePrestamo> list = pm.reportePrestamoTotal();
        for (ReportePrestamo rp : list) {
            System.out.print("Este es el reporte total de los prestamos. Cantidad de prestamos: " + rp.getCantidad()
                    + " Total de dinero prestado entre todos los prestamos: " + rp.getTotal());

        }

    }

    public ReporteService(PrestamoManager pm) {
        this.pm = pm;
    }

    public PrestamoManager getPm() {
        return pm;
    }

    public void setPm(PrestamoManager pm) {
        this.pm = pm;
    }

}
