package com.google.maps;

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

public class GeocodeInterface
{
    private final GeoApiContext apiContext = ClientDeliveryController.apiContext;

    public GeocodingResult[] GetAddress(double lat, double lng)
    {
        GeocodingResult[] results = null;
        LatLng latLng = new LatLng(lat, lng);

        try
        {
            results = GeocodingApi.newRequest(apiContext).latlng(latLng).await();
            assertNotNull(results);
            assertNotNull(Arrays.toString(results));
        }
        catch(ApiException | InterruptedException | IOException aEx)
        {
            System.out.println("Geocode Interface: ");
            System.out.println(aEx.getMessage());
        }
        return results;
    }


    public GeocodingResult[] GetCoordinates(String addr)
    {
        GeocodingResult[] results = null;

        try
        {
            results = GeocodingApi.newRequest(apiContext).address(addr).await();
            assertNotNull(results);
            assertNotNull(Arrays.toString(results));
        }
        catch(ApiException | InterruptedException | IOException ex)
        {
            System.out.println("Geocode Interface: ");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return results;
    }
}
