package com.google.maps;

import java.io.InputStream;
import java.util.Properties;
import java.net.URL;

//loads the api key from a non-public property
public class BuildGeoApiContext
{

    public GeoApiContext BuildContext()
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

        return context;
    }
}
