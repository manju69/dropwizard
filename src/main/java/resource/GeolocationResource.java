package resource;

import com.codahale.metrics.annotation.Timed;
import repository.GeolocationDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/geolocation")
public class GeolocationResource {
    private final GeolocationDAO geolocationDAO;
    public GeolocationResource(GeolocationDAO geolocationDAO) {
        this.geolocationDAO = geolocationDAO;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public String getGeolocation(@QueryParam("ipAddress") Optional<String> ipAddress) {
        return "Testing dropwizard application" +ipAddress;
    }
}
