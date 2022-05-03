package service;

import domain.Geolocation;
import domain.dto.GeolocationDTO;
import exception.ClientDataException;
import repository.GeolocationDAO;
import service.adapter.GeolocationAdapter;

import javax.ws.rs.client.Client;
import java.time.LocalDateTime;


public class GeolocationService implements IGeolocationService{
    private final String getURL = "http://ip-api.com/json/";
    private final GeolocationDAO geolocationDAO;
    private Client client;

    public GeolocationService(GeolocationDAO geolocationDAO, Client client){
        this.geolocationDAO = geolocationDAO;
        this.client = client;
    }

    public GeolocationDTO getGeoDataFromAPI(String ipAddress){
        GeolocationDTO geolocationDTO;
        Geolocation geoData;
        try{
           // log.info("Ip address Searching in Database: "+ipAddress);
            geoData = getGeoDataFromDB(ipAddress);
            if(geoData==null)
                System.out.println("not in database");
            if(LocalDateTime.now().minusMinutes(5l).isAfter(geoData.getUpdateTime())){
                System.out.println("5 min timer");
                //log.info("Refreshing data in 5 minutes by calling external API");
                geoData =  client.target(getURL+ipAddress).request().get().readEntity(Geolocation.class);
            }
            geolocationDTO = GeolocationAdapter.get(geoData);
        }
        catch (ClientDataException c){
           // log.info("External API is called to search IP address: "+ipAddress);
            System.out.println("calling api");
            geoData = client.target(getURL+ipAddress).request().get().readEntity(Geolocation.class);
            if(geoData==null){
                System.out.println("not found in api");
            }
            if(geoData.getStatus().equalsIgnoreCase("success")){
                saveGeoData(ipAddress,geoData);
            }
            geolocationDTO = GeolocationAdapter.get(geoData);
        }
        return geolocationDTO;
    }

    public void saveGeoData(String ipAddress, Geolocation geolocation){
        //log.info("save geolocation data in database with ip address: "+ipAddress);
        geolocation.setIpAddress(ipAddress);
        geolocationDAO.save(geolocation);
    }

    public Geolocation getGeoDataFromDB (String ipAddress){
        //log.info("Database is called to search Ip address: "+ipAddress);
        Geolocation geolocationOpt = geolocationDAO.findByIpAddress(ipAddress);
        if(geolocationOpt!=null){
            System.out.println("Record found in database");
            //log.info("Record found in database");
        }else{
            //log.info("Record not found in database: need to call external API");
            System.out.println("Record not found in database: need to call external API");;
            throw new ClientDataException("Ip address not present in database");
        }
        return geolocationOpt;
    }
}
