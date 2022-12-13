package com.google.maps;

//import static org.hamcrest.CoreMatchers.not;
//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThat;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.Arrays;

public class DirectionsInterface
{
    private final GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyATEPZwUuEBs8kAi_TGAUL7_i1qpIr03rY")
            .build();

    public DirectionsResult GetDirections(String origin, String destination)
    {
        DirectionsResult result = null;
        try
        {
            result = DirectionsApi.getDirections(context, origin, destination).await();
            assertNotNull(result);
            assertNotNull(result.geocodedWaypoints);
            assertNotNull(Arrays.toString(result.geocodedWaypoints));
        }
        catch(ApiException | InterruptedException | IOException aEx)
        {
            System.out.println("Directions Interface: ");
            System.out.println(aEx.getMessage());
        }
        context.shutdown();
        return result;
    }
}
