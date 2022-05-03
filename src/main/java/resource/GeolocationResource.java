package resource;

import com.codahale.metrics.annotation.Timed;
import domain.dto.ApiResponse;
import domain.dto.GeolocationDTO;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.http.HttpStatus;
import service.GeolocationService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

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
    public Response getGeolocation(@PathParam("ipAddress") String ipAddress) {
        System.out.println("inside get request"+ipAddress);
            GeolocationDTO geolocationDTO = geolocationService.getGeoDataFromAPI(ipAddress);
        ApiResponse<Object, Object> response;
        if(geolocationDTO.getStatus().equalsIgnoreCase("success")){
            response = ApiResponse.success(geolocationDTO, "Geolocation found");
        }else
            response = ApiResponse.failed(HttpStatus.SC_NOT_FOUND,"Geolocation not found in API");

            return Response.status(200).entity(response).build();
    }
}
