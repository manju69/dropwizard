package service;

import domain.Geolocation;
import domain.dto.GeolocationDTO;
import exception.ClientDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.GeolocationDAO;
import service.adapter.GeolocationAdapter;

import javax.ws.rs.client.Client;
import java.time.LocalDateTime;



public class GeolocationService implements IGeolocationService{
    private final String getURL = "http://ip-api.com/json/";
    private final GeolocationDAO geolocationDAO;
    private Client client;
    private static Logger log = LoggerFactory.getLogger(GeolocationService.class);

    public GeolocationService(GeolocationDAO geolocationDAO, Client client){
        this.geolocationDAO = geolocationDAO;
        this.client = client;
    }


    public GeolocationDTO getGeoDataFromAPI(String ipAddress){
        //*----------------------------------------*/
//        Geolocation geo = geolocationDAO.getFromQuery(ipAddress);
//        if(geo==null){
//            System.out.println("the value is null");;
//        }
        //*----------------------------------------*/
        GeolocationDTO geolocationDTO;
        Geolocation geoData;
        try{
           log.info("Ip address Searching in Database: "+ipAddress);
            geoData = getGeoDataFromDB(ipAddress);
            if(LocalDateTime.now().minusMinutes(5l).isAfter(geoData.getUpdateTime())){
                log.info("Refreshing data in 5 minutes by calling external API");
                geoData =  client.target(getURL+ipAddress).request().get().readEntity(Geolocation.class);
            }
            geolocationDTO = GeolocationAdapter.get(geoData);
        }
        catch (ClientDataException c){
           log.info("External API is called to search IP address: "+ipAddress);
            geoData = client.target(getURL+ipAddress).request().get().readEntity(Geolocation.class);
            if(geoData.getStatus().equalsIgnoreCase("success")){
                saveGeoData(ipAddress,geoData);
            }
            geolocationDTO = GeolocationAdapter.get(geoData);
        }
        return geolocationDTO;
    }

    public void saveGeoData(String ipAddress, Geolocation geolocation){
        log.info("save geolocation data in database with ip address: "+ipAddress);
        geolocation.setIpAddress(ipAddress);
        geolocationDAO.save(geolocation);
    }

    public Geolocation getGeoDataFromDB (String ipAddress){
        log.info("Database is called to search Ip address: "+ipAddress);
        Geolocation geolocationOpt = geolocationDAO.findByIpAddress(ipAddress);
        if(geolocationOpt!=null){
            log.info("Record found in database");
        }else{
            log.info("Record not found in database: need to call external API");
            throw new ClientDataException("Ip address not present in database");
        }
        return geolocationOpt;
    }
}
