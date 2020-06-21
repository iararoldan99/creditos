package ar.com.ada.creditos.managers;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import ar.com.ada.creditos.entities.*;
import ar.com.ada.creditos.reportes.ReporteCliente;
import ar.com.ada.creditos.reportes.ReportePrestamo;

public class PrestamoManager {

    protected SessionFactory sessionFactory;

    public void setup() {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings
                                                                                                  // from
                                                                                                  // hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw ex;
        }

    }

    public void exit() {
        sessionFactory.close();
    }

    public void create(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    public Prestamo read(int prestamoId) {
        Session session = sessionFactory.openSession();

        Prestamo prestamo = session.get(Prestamo.class, prestamoId);

        session.close();

        return prestamo;
    }

    public void update(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Este metodo en la vida real no debe existir ya qeu puede haber miles de
     * usuarios
     * 
     * @return
     */
    public List<Prestamo> buscarTodos() {

        Session session = sessionFactory.openSession();

        /// NUNCA HARCODEAR SQLs nativos en la aplicacion.
        // ESTO es solo para nivel educativo
        Query query = session.createNativeQuery("SELECT * FROM prestamo", Prestamo.class);

        List<Prestamo> todos = query.getResultList();

        return todos;

    }

    /**
     * Busca una lista de clientes por el nombre completo Esta armado para que se
     * pueda generar un SQL Injection y mostrar commo NO debe programarse.
     * 
     * @param nombre
     * @return
     */
    public List<Prestamo> buscarPor(String nombre) {

        Session session = sessionFactory.openSession();

        // SQL Injection vulnerability exposed.
        // Deberia traer solo aquella del nombre y con esto demostrarmos que trae todas
        // si pasamos
        // como nombre: "' or '1'='1"
        Query query = session.createNativeQuery("SELECT * FROM cliente where nombre = '" + nombre + "'", Cliente.class);

        List<Prestamo> prestamos = query.getResultList();

        return prestamos;

    }

    // ReporteCliente devuelve una lista de tipo Cliente, que al pasarle el
    // clienteID por parametro,
    // crea un query nativo donde selecciona el id y nombre del cliente, la cantidad
    // de prestamos
    // el prestamo de mayor importe y la suma de todos los prestamos que tiene ese
    // cliente

    public List<ReporteCliente> reporteCliente(int clienteID) {
        Session session = sessionFactory.openSession();

        Query reporteCliente = session.createNativeQuery(
                "select c.cliente_id, c.nombre, count(*) cantidad, max(p.importe) maximo, sum(p.importe) total from cliente c inner join prestamo p on c.cliente_id = p.cliente_id where c.cliente_id = ? group by c.cliente_id, c.nombre",
                ReporteCliente.class);
        reporteCliente.setParameter(1, clienteID);

        List<ReporteCliente> prestamosCliente = reporteCliente.getResultList();

        return prestamosCliente;

    };

    public List<ReportePrestamo> reportePrestamoTotal() {
        Session session = sessionFactory.openSession();

        Query reportePrestamo = session.createNativeQuery(
                "Select count(*) cantidad, sum(p.importe) total From prestamo p", ReportePrestamo.class);

        List<ReportePrestamo> prestamosTotal = reportePrestamo.getResultList();

        return prestamosTotal;

    }

    public Prestamo buscarPrestamoPorId(int prestamoId) {
        Session session = sessionFactory.openSession();

        Query buscarPrestamoId = session.createNativeQuery("SELECT * FROM prestamo where prestamo_id = ?",
                Prestamo.class);
        buscarPrestamoId.setParameter(1, prestamoId);

        Prestamo buscarPrestamo = (Prestamo) buscarPrestamoId.getSingleResult();

        return buscarPrestamo;

    }

}
