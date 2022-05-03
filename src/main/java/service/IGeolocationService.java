package service;

import domain.Geolocation;
import domain.dto.GeolocationDTO;

public interface IGeolocationService {
    public GeolocationDTO getGeoDataFromAPI(String ipAddress);
    public void saveGeoData(String ipAddress, Geolocation geolocation);
    public Geolocation getGeoDataFromDB(String ipAddress);
}
