package ar.com.ada.creditos;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.html.CSS;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import ar.com.ada.creditos.entities.*;
import ar.com.ada.creditos.excepciones.*;
import ar.com.ada.creditos.managers.*;
import ar.com.ada.creditos.reportes.ReporteCliente;
import ar.com.ada.creditos.services.ReporteService;

public class ABM {

    public static Scanner Teclado = new Scanner(System.in);

    protected ClienteManager ABMCliente = new ClienteManager();

    protected PrestamoManager ABMPrestamo = new PrestamoManager();

    protected CancelacionManager ABMCancelacion = new CancelacionManager(null, ABMCliente, ABMPrestamo);

    protected ReporteService reporteService = new ReporteService(ABMPrestamo);

    public void iniciar() throws Exception {

        try {

            ABMCliente.setup();
            ABMPrestamo.setup();
            ABMCancelacion.setup();
            // reporteService.setPm(ABMPrestamo); opcion 2 para usar el manager creado

            printOpciones();

            int opcion = Teclado.nextInt();
            Teclado.nextLine();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        try {
                            alta();
                        } catch (ClienteDNIException exdni) {
                            System.out.println("Error en el DNI. Indique uno valido");
                        }
                        break;

                    case 2:
                        baja();
                        break;

                    case 3:
                        modifica();
                        break;

                    case 4:
                        listar();
                        break;

                    case 5:
                        listarPorNombre();
                        break;

                    case 6:
                        agregarPrestamoCliente();
                        break;

                    case 7:
                        listarPrestamos();
                        break;

                    case 8:
                        imprimirReporteCliente();
                        break;

                    case 9:
                        reportePrestamos();
                        break;

                    case 10:
                        cancelarUnPrestamo();
                        break;

                    case 11:
                        eliminacionLogicaCancelacion();
                        break;

                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printOpciones();

                opcion = Teclado.nextInt();
                Teclado.nextLine();
            }

            // Hago un safe exit del manager
            ABMCliente.exit();
            ABMPrestamo.exit();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Que lindo mi sistema,se rompio mi sistema");
            throw e;
        } finally {
            System.out.println("Saliendo del sistema, bye bye...");

        }

    }

    public void alta() throws Exception {
        Cliente cliente = new Cliente();
        System.out.println("Ingrese el nombre:");
        cliente.setNombre(Teclado.nextLine());
        System.out.println("Ingrese el DNI:");
        cliente.setDni(Teclado.nextInt());
        Teclado.nextLine();
        System.out.println("Ingrese el domicilio:");
        cliente.setDomicilio(Teclado.nextLine());

        System.out.println("Ingrese el Domicilio alternativo(OPCIONAL):");

        String domAlternativo = Teclado.nextLine();

        if (domAlternativo != null)
            cliente.setDomicilioAlternativo(domAlternativo);

        Prestamo prestamo = new Prestamo();
        BigDecimal importePrestamo = new BigDecimal(5000);
        prestamo.setImporte(importePrestamo);
        prestamo.setFecha(new Date());
        prestamo.setFechaAlta(new Date());
        prestamo.setCuotas(10);
        prestamo.setCliente(cliente);

        ABMCliente.create(cliente);

        /*
         * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
         * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
         * no para loguear info Lo mejor es usar:
         * System.out.println("Cliente generada con exito.  " + cliente.getClienteId);
         */

        System.out.println("Cliente generada con exito.  " + cliente);

    }

    public void baja() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el ID de Cliente:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Cliente clienteEncontrado = ABMCliente.read(id);

        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado.");

        } else {

            try {

                ABMCliente.delete(clienteEncontrado);
                System.out
                        .println("El registro del cliente " + clienteEncontrado.getClienteId() + " ha sido eliminado.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una cliente. Error: " + e.getCause());
            }

        }
    }

    public void bajaPorDNI() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el DNI de Cliente:");
        String dni = Teclado.nextLine();
        Cliente clienteEncontrado = ABMCliente.readByDNI(dni);

        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado.");

        } else {
            ABMCliente.delete(clienteEncontrado);
            System.out.println("El registro del DNI " + clienteEncontrado.getDni() + " ha sido eliminado.");
        }
    }

    public void modifica() throws Exception {
        // System.out.println("Ingrese el nombre de la cliente a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID de la cliente a modificar:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Cliente clienteEncontrado = ABMCliente.read(id);

        if (clienteEncontrado != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(clienteEncontrado.toString() + " seleccionado para modificacion.");

            System.out.println(
                    "Elija qué dato de la cliente desea modificar: \n1: nombre, \n2: DNI, \n3: domicilio, \n4: domicilio alternativo");
            int selecper = Teclado.nextInt();

            switch (selecper) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    Teclado.nextLine();
                    clienteEncontrado.setNombre(Teclado.nextLine());

                    break;
                case 2:
                    System.out.println("Ingrese el nuevo DNI:");
                    Teclado.nextLine();
                    clienteEncontrado.setDni(Teclado.nextInt());
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo domicilio:");
                    Teclado.nextLine();
                    clienteEncontrado.setDomicilio(Teclado.nextLine());

                    break;
                case 4:
                    System.out.println("Ingrese el nuevo domicilio alternativo:");
                    Teclado.nextLine();
                    clienteEncontrado.setDomicilioAlternativo(Teclado.nextLine());

                    break;

                default:
                    break;
            }

            // Teclado.nextLine();

            ABMCliente.update(clienteEncontrado);

            System.out.println("El registro de " + clienteEncontrado.getNombre() + " ha sido modificado.");

        } else {
            System.out.println("Cliente no encontrado.");
        }

    }

    public void listar() {

        List<Cliente> todos = ABMCliente.buscarTodos();
        for (Cliente c : todos) {
            mostrarCliente(c);
        }
    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Cliente> clientes = ABMCliente.buscarPor(nombre);
        for (Cliente cliente : clientes) {
            mostrarCliente(cliente);
        }
    }

    public void mostrarCliente(Cliente cliente) {

        System.out.print("Id: " + cliente.getClienteId() + " Nombre: " + cliente.getNombre() + " DNI: "
                + cliente.getDni() + " Domicilio: " + cliente.getDomicilio());

        if (cliente.getDomicilioAlternativo() != null)
            System.out.println(" Alternativo: " + cliente.getDomicilioAlternativo());
        else
            System.out.println();
    }

    public static void printOpciones() {
        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Para agregar un cliente.");
        System.out.println("2. Para eliminar un cliente.");
        System.out.println("3. Para modificar un cliente.");
        System.out.println("4. Para ver el listado.");
        System.out.println("5. Buscar un cliente por nombre especifico(SQL Injection)).");
        System.out.println("6. Agregar un prestamo a un cliente");
        System.out.println("7. Listar todos los prestamos");
        System.out.println("8. Reporte de un cliente");
        System.out.println("9. Reporte de prestamos");
        System.out.println("10. Cancelar un prestamo");
        System.out.println("11. Eliminar una cancelacion(un pago)");
        System.out.println("12. Ver la lista de cancelaciones");
        System.out.println("0. Para terminar.");
        System.out.println("");
        System.out.println("=======================================");
    }

    public void agregarPrestamoCliente() { // se debe poder agregar un prestamo a un cliente
        System.out.println("Ingrese el ID del Cliente");
        int clienteID = Teclado.nextInt();
        Cliente cl = ABMCliente.read(clienteID);
        if (cl == null) {
            System.out.println("Cliente no existe");
            return;
        }
        Prestamo p = new Prestamo();
        System.out.println("Ingrese el monto");
        p.setImporte(Teclado.nextBigDecimal());
        p.setCliente(cl);
        System.out.println("Ingrese las cuotas");
        p.setCuotas(Teclado.nextInt());
        System.out.println("Inserte la fecha del prestamo: ");
        System.out.println("Inserte el Año : ");
        int anioAlta = Teclado.nextInt();
        Teclado.nextLine();
        System.out.println("Inserte el mes : ");
        int mesAlta = Teclado.nextInt();
        Teclado.nextLine();
        System.out.println("Inserte el Dia : ");
        int diaAlta = Teclado.nextInt();
        Teclado.nextLine();
        p.setFecha(new Date(anioAlta, mesAlta, diaAlta));
        p.setFechaAlta(new Date());

        ABMPrestamo.create(p);

    }

    public void listarPrestamos() { // que imprima todos los prestamos con sus respectivos clientes
        List<Prestamo> todos = ABMPrestamo.buscarTodos();
        for (Prestamo p : todos) {
            mostrarPrestamo(p);
        }

    }

    public void mostrarPrestamo(Prestamo prestamo) {

        System.out.print("Id: " + prestamo.getPrestamoId() + " Cliente: " + prestamo.getCliente() + " Importe: "
                + prestamo.getImporte() + " Cuotas: " + prestamo.getCuotas() + "Fecha: " + prestamo.getFecha());

    }

    public void imprimirReporteCliente() {

        System.out.println("Ingrese el id del cliente: ");
        int clienteID = Teclado.nextInt();
        reporteService.mostrarReporteCliente(clienteID);
    }

    public void reportePrestamos() {
        reporteService.mostrarReportePrestamoTotal();
    }

    public void cancelarUnPrestamo() {
        System.out.println("Ingrese el ID del prestamo que desea cancelar");
        int prestamoId = Teclado.nextInt();
        Prestamo pcan = ABMPrestamo.read(prestamoId);
        if (pcan == null) {
            System.out.println("El prestamo que desea cancelar no existe");
            return;
        }
        Cancelacion c = new Cancelacion();
        c.setPrestamoId(ABMPrestamo.buscarPrestamoPorId(prestamoId));
        System.out.println("Ingrese el importe a pagar: ");
        c.setImporte(Teclado.nextBigDecimal());
        System.out.println("Ingrese las cuotas: ");
        c.setCuota(Teclado.nextInt());
        System.out.println("Inserte la fecha de cancelacion: ");
        System.out.println("Inserte el Año: ");
        int anioCancelacion = Teclado.nextInt();
        Teclado.nextLine();
        System.out.println("Inserte el mes: ");
        int mesCancelacion = Teclado.nextInt();
        Teclado.nextLine();
        System.out.println("Inserte el dia: ");
        int diaCancelacion = Teclado.nextInt();
        Teclado.nextLine();
        c.setFechaCancelacion(new Date(anioCancelacion, mesCancelacion, diaCancelacion));

        ABMCancelacion.create(c);

        System.out.println("La cancelacion ha sido exitosa");

    }

    public void eliminacionLogicaCancelacion(){ //borrado LOGICO
        System.out.println("Ingrese el ID de la cancelacion que desea borrar");
        int cancelacionId = Teclado.nextInt();
        Cancelacion can = ABMCancelacion.read(cancelacionId);
        if (can == null) {
            System.out.println("La cancelacion que desea borrar no existe");
            return;
        }

        ABMCancelacion.borradoLogico(can);

        System.out.println("Se ha eliminado el pago de forma exitosa");

    }

    public void eliminarCancelacion() { //borrado FISICO
        System.out.println("Ingrese el ID de la cancelacion que desea borrar");
        int cancelacionId = Teclado.nextInt();
        Cancelacion can = ABMCancelacion.read(cancelacionId);
        if (can == null) {
            System.out.println("La cancelacion que desea borrar no existe");
            return;
        }

        ABMCancelacion.delete(can);

        System.out.println("Se ha eliminado el pago de forma exitosa");

    }

}