package configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;

import javax.validation.Valid;

public class GeolocationConfiguration extends Configuration {

    @Valid
    private PooledDataSourceFactory dataSourceFactory = new DataSourceFactory();

    @JsonProperty("database")
    public PooledDataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

//    @Valid
//    @NotNull
//    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();
//
//    @JsonProperty("jerseyClient")
//    public JerseyClientConfiguration getJerseyClientConfiguration() {
//        return jerseyClient;
//    }
//
//    @JsonProperty("jerseyClient")
//    public void setJerseyClientConfiguration(JerseyClientConfiguration jerseyClient) {
//        this.jerseyClient = jerseyClient;
//    }
}
