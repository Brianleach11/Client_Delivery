package com.google.maps;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class ThreadingApisForCoords extends Thread
{
    private volatile GeocodingResult[] results;
    public void run(String input)
    {
        GeocodeInterface geocodeInterface = new GeocodeInterface();
        try
        {
            results = geocodeInterface.GetCoordinates(input);
        }
        catch(Exception ex)
        {
            System.out.println("Threading Apis: ");
            System.out.println(ex.getMessage());
        }
    }

    public LatLng GetResults()
    {
        return new LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng);
    }
}
