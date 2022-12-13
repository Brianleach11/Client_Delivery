package com.google.maps;

//import static com.google.maps.TestUtils.retrieveBody;
//import static com.google.maps.model.ComponentFilter.administrativeArea;
//import static com.google.maps.model.ComponentFilter.country;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.fail;

import com.google.maps.errors.ApiException;
//import com.google.maps.internal.HttpHeaders;
//import com.google.maps.model.AddressComponentType;
//import com.google.maps.model.AddressType;
//import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
//import com.google.maps.model.LocationType;

import java.io.IOException;
//import java.util.ArrayList;
import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//import okhttp3.Headers;
//import org.junit.Test;
//import org.junit.experimental.categories.Category;

public class GeocodeInterface
{
    private final GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("YourApiKey")
            .build();

    public GeocodingResult[] GetAddress(Double lat, Double lng)
    {
        GeocodingResult[] results = null;
        LatLng latLng = new LatLng(lat, lng);

        try
        {
            results = GeocodingApi.newRequest(context).latlng(latLng).await();
            assertNotNull(results);
            assertNotNull(Arrays.toString(results));
        }
        catch(ApiException | InterruptedException | IOException aEx)
        {
            System.out.println("Geocode Interface: ");
            System.out.println(aEx.getMessage());
        }

        context.shutdown();
        return results;
    }


    public GeocodingResult[] GetCoordinates(String addr)
    {
        GeocodingResult[] results = null;

        try
        {
            results = GeocodingApi.newRequest(context).address(addr).await();
            assertNotNull(results);
            assertNotNull(Arrays.toString(results));
        }
        catch(ApiException aEx)
        {
            System.out.println("Geocode Interface: ");
            System.out.println(aEx.getMessage());
        }
        catch(InterruptedException iEx)
        {
            System.out.println("Geocode Interface: ");
            System.out.println(iEx.getMessage());
        }
        catch(IOException ioEx)
        {
            System.out.println("Geocode Interface: ");
            System.out.println(ioEx.getMessage());
        }

        return results;
    }
}
