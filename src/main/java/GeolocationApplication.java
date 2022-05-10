
import caching.GeolocationCaching;
import configuration.GeolocationConfiguration;
import domain.Geolocation;
import exception.InputValidation;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import repository.GeolocationDAO;
import resource.GeolocationResource;
import service.GeolocationService;
import service.IGeolocationService;

import javax.ws.rs.client.Client;

public class GeolocationApplication extends Application<GeolocationConfiguration> {
    public static void main(String[] args) throws Exception {

        new GeolocationApplication().run(args);
    }
    @Override
    public String getName() {
        return "demo";
    }

    @Override
    public void initialize(Bootstrap<GeolocationConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(GeolocationConfiguration geolocationConfiguration, Environment environment) throws Exception {
        final GeolocationCaching cacheInstance = GeolocationCaching.getInstance();
        final GeolocationDAO geolocationDAO = new GeolocationDAO(hibernate.getSessionFactory());
        final Client client = new JerseyClientBuilder(environment).using(new JerseyClientConfiguration())
                .build(getName());
        final IGeolocationService geolocationService = new GeolocationService(geolocationDAO,client);
        cacheInstance.initGeoCache(geolocationService);
        final GeolocationResource resource = new GeolocationResource(geolocationService);
        environment.jersey().register(new InputValidation());
        environment.jersey().register(resource);
    }

    private final HibernateBundle<GeolocationConfiguration> hibernate = new HibernateBundle<GeolocationConfiguration>(Geolocation.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(GeolocationConfiguration geolocationConfiguration) {
            return geolocationConfiguration.getDataSourceFactory();
        }
    };
}
