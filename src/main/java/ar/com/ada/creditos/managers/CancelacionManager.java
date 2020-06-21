package ar.com.ada.creditos.managers;

import java.util.logging.Level;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import ar.com.ada.creditos.entities.Cancelacion;

public class CancelacionManager {

    /*
     * 2) Que se pueda eliminar un pago a un préstamo(en forma LOGICA) 3) Reportes
     * sobre cancelaciones(usar los reportes que hayamos hecho) y encapsular!
     */

    protected SessionFactory sessionFactory;
    protected ClienteManager ABMCliente = new ClienteManager();
    protected PrestamoManager ABMPrestamo = new PrestamoManager();

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

    public CancelacionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public CancelacionManager(SessionFactory sessionFactory, ClienteManager aBMCliente, PrestamoManager aBMPrestamo) {
        this.sessionFactory = sessionFactory;
        ABMCliente = aBMCliente;
        ABMPrestamo = aBMPrestamo;
    }

    public void exit() {
        sessionFactory.close();
    }

    public void create(Cancelacion cancelacion) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(cancelacion);

        session.getTransaction().commit();
        session.close();
    }

    public Cancelacion read(int cancelacionId) {
        Session session = sessionFactory.openSession();

        Cancelacion cancelacion = session.get(Cancelacion.class, cancelacionId);

        session.close();

        return cancelacion;
    }

    public void update(Cancelacion cancelacion) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(cancelacion);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Cancelacion cancelacion) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(cancelacion);

        session.getTransaction().commit();
        session.close();
    }

    public Cancelacion buscarCancelacionPorId(int cancelacionId) {
        Session session = sessionFactory.openSession();

        Query buscarCancelacionId = session.createNativeQuery("SELECT * FROM cancelacion where cancelacion_id = ?",
                Cancelacion.class);
        buscarCancelacionId.setParameter(1, cancelacionId);

        Cancelacion buscarCancelacion = (Cancelacion) buscarCancelacionId.getSingleResult();

        return buscarCancelacion;

    }

    public Cancelacion borradoLogico(Cancelacion cancelacionId) {
        Session session = sessionFactory.openSession();

        Query borraLogico = session.createNativeQuery("UPDATE cancelacion SET status = 0 where cancelacion_id = ?",
                Cancelacion.class);
        borraLogico.setParameter(1, cancelacionId);

        Cancelacion borradoLog = (Cancelacion) borraLogico.getSingleResult();

        return borradoLog;

    }

    

}