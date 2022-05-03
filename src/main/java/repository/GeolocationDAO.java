package repository;

import domain.Geolocation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class GeolocationDAO extends AbstractDAO<Geolocation> {
    public GeolocationDAO(SessionFactory sessionFactory){
        super(sessionFactory);
    }

    public Geolocation findByIpAddress(String ipAddress){
        return get(ipAddress);
    }

    public void save(Geolocation geolocation) {
        persist(geolocation);
    }
}
