package resource;

import caching.GeolocationCaching;
import com.codahale.metrics.annotation.Timed;
import domain.dto.ApiResponse;
import domain.dto.GeolocationDTO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;
import org.apache.http.HttpStatus;
import service.GeolocationService;

import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;


@Path("/geolocation")
@Produces(MediaType.APPLICATION_JSON)
public class GeolocationResource {
    private final GeolocationService geolocationService;
    public GeolocationResource(GeolocationService geolocationService)
    {
        this.geolocationService = geolocationService;
    }

    @GET
    @Timed
    @Path("/{ipAddress}")
    @UnitOfWork
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.MINUTES)
    public Response getGeolocation(@Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$",
            message = "IP address must be in the format: 0.0.0.0 to 255.255.255.255") @PathParam("ipAddress") String ipAddress) {

        GeolocationDTO geolocationDTO = GeolocationCaching.getInstance().getGeolocationDataFromCache(ipAddress);;
        ApiResponse<Object, Object> response;
        if(geolocationDTO.getStatus().equalsIgnoreCase("success")){
            response = ApiResponse.success(geolocationDTO, "Geolocation found");
        }else
            response = ApiResponse.failed(HttpStatus.SC_NOT_FOUND,"Geolocation not found in API");

            return Response.status(200).entity(response).build();
    }
}
