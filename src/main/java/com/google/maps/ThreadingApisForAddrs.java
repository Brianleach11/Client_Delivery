package com.google.maps;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class ThreadingApisForAddrs extends Thread
{
    private volatile GeocodingResult[] results;

    public void run(LatLng input)
    {
        GeocodeInterface geocodeInterface = new GeocodeInterface();
        try
        {
            results = geocodeInterface.GetAddress(input.lat, input.lng);
        }
        catch(Exception ex)
        {
            System.out.println("Threading Apis: ");
            System.out.println(ex.getMessage());
        }
    }

    public String GetResults()
    {
        return results[0].formattedAddress;
    }
}
