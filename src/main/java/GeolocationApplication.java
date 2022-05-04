
import caching.GeolocationCaching;
import configuration.GeolocationConfiguration;
import domain.Geolocation;
import exception.InputValidation;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.GeolocationDAO;
import resource.GeolocationResource;
import service.GeolocationService;

import javax.ws.rs.client.Client;
import java.util.concurrent.TimeUnit;

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
        final Client client = new JerseyClientBuilder(environment).using(geolocationConfiguration.getJerseyClientConfiguration())
                .build(getName());
        final GeolocationService geolocationService = new GeolocationService(geolocationDAO,client);
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
