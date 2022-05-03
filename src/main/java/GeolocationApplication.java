
import configuration.GeolocationConfiguration;
import domain.Geolocation;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import repository.GeolocationDAO;
import resource.GeolocationResource;
import service.GeolocationService;

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
        GeolocationDAO geolocationDAO = new GeolocationDAO(hibernate.getSessionFactory());
        final Client client = new JerseyClientBuilder(environment).using(geolocationConfiguration.getJerseyClientConfiguration())
                .build(getName());
        GeolocationService geolocationService = new GeolocationService(geolocationDAO,client);
        final GeolocationResource resource = new GeolocationResource(geolocationService);
        environment.jersey().register(resource);
    }
    private final HibernateBundle<GeolocationConfiguration> hibernate = new HibernateBundle<GeolocationConfiguration>(Geolocation.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(GeolocationConfiguration geolocationConfiguration) {
            return geolocationConfiguration.getDataSourceFactory();
        }
    };
}
