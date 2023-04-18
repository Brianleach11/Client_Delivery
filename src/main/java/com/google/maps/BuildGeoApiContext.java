package com.google.maps;

import java.io.InputStream;
import java.util.Properties;

//loads the api key from a non-public property
public class BuildGeoApiContext
{
    private final Properties props = new Properties();

    private final InputStream input = GeocodeInterface.class.getClassLoader().getResourceAsStream("scratch.properties");

    private String apiKey;

    public GeoApiContext BuildContext()
    {
        GeoApiContext context;
        try
        {
            context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();
            props.load(input);
            apiKey = props.getProperty("api.key");
        }catch(Exception ex)
        {
            context = null;
            ex.printStackTrace();
        }

        return context;
    }
}
