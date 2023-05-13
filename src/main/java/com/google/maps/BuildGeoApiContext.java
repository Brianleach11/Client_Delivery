package com.google.maps;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.InputStream;
import java.util.Properties;

//loads the api key from a non-public property
@Component
@RequestScope
public class BuildGeoApiContext
{
    private GeoApiContext apiContext;

    public BuildGeoApiContext()
    {
        Properties props = new Properties();
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("ApiConnection.properties");
        GeoApiContext context;
        try
        {
            props.load(input);
            String apiKey = props.getProperty("api.key");

            context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();
        }catch(Exception ex)
        {
            context = null;
            ex.printStackTrace();
        }

        this.apiContext = context;
    }
    public GeoApiContext get()
    {
        return apiContext;
    }
    @PreDestroy
    public void close()
    {
        apiContext.shutdown();
    }
}
