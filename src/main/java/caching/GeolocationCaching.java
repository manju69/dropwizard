package caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import domain.dto.GeolocationDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GeolocationService;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GeolocationCaching {
    private static final Logger logger = LoggerFactory.getLogger(GeolocationCaching.class);
    private static GeolocationCaching geolocationCaching = new GeolocationCaching();

    public static GeolocationCaching getInstance() {
        return geolocationCaching;
    }

    private static LoadingCache<String, GeolocationDTO> geoCache;

    public void initGeoCache(GeolocationService geolocationService) {
        if (geoCache == null) {
            geoCache = CacheBuilder.newBuilder()
                            .concurrencyLevel(10)
                            .maximumSize(200) // Maximum of 200 records can be cached
                            .expireAfterAccess(1, TimeUnit.MINUTES) // Cache will expire after 30 minutes
                            .recordStats()
                            .build(new CacheLoader<String, GeolocationDTO>() { // Build the CacheLoader
                                @Override
                                public GeolocationDTO load(String ipAddress) throws Exception {
                                    logger.info("Fetching Geolocation Data from DB/ Cache");
                                    return geolocationService.getGeoDataFromAPI(ipAddress);
                                }
                            });
        }
    }

    public GeolocationDTO getGeolocationDataFromCache(String key) {
        try {
            CacheStats cacheStats = geoCache.stats();
            logger.info("CacheStats = {} ", cacheStats);
            return geoCache.get(key);
        } catch (ExecutionException e) {
            logger.error("Error Retrieving Elements from the Student Cache"
                    + e.getMessage());
        }
        return null;
    }


}
