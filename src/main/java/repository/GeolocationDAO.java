package repository;

import domain.Geolocation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.Query;

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

    public Geolocation getFromQuery(String ipAddress){
        String geoquery = "select * from geolocation where query ="+ipAddress;
        Query query = currentSession().createQuery(geoquery);
        return (Geolocation) query.getSingleResult();
    }
}
