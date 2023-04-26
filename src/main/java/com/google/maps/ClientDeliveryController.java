package com.google.maps;

import com.google.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;


public class ClientDeliveryController
{
    public static final GeoApiContext apiContext = new BuildGeoApiContext().BuildContext();
    private final static ThreadingApisForCoords threadingApisForCoords = new ThreadingApisForCoords();
    private final static ThreadingApisForAddrs threadingApisForAddrs = new ThreadingApisForAddrs();


    public static void main(String[] args)
    {
        ArrayList<String> inputs = GetInput();
        ArrayList<LatLng> latLngs = new ArrayList<>();
        try
        {
            for (String input: inputs)
            {
                threadingApisForCoords.run(input);
                threadingApisForCoords.join();
                latLngs.add(threadingApisForCoords.GetResults());
            }
        }
        catch(InterruptedException interruptedException)
        {
            System.out.println("Threading for Coords");
            System.out.println(interruptedException.getMessage());
        }

        assertNotNull(latLngs);

        ArrayList<String> addresses = new ArrayList<>();

        try
        {
            for (LatLng latLng : latLngs)
            {
                System.out.println("Latitude: " + latLng.lat + " Longitude: " + latLng.lng);
                threadingApisForAddrs.run(latLng);
                threadingApisForAddrs.join();
                addresses.add(threadingApisForAddrs.GetResults());
            }
        }
        catch(InterruptedException interruptedException)
        {
            System.out.println("Threading for Addrs");
            System.out.println(interruptedException.getMessage());
        }

        assertNotNull(addresses);

        for(String addr : addresses)
        {
            System.out.println("Address:");
            System.out.println(addr);
        }

        apiContext.shutdown();
    }

    private static ArrayList<String> GetInput()
    {
        ArrayList<String> inputs = new ArrayList<>();
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String homeAddress, input;
            System.out.println("Enter a home address:");
            homeAddress = br.readLine();
            inputs.add(homeAddress);
            while(true)
            {
                System.out.println("Enter address or 'done':");
                input = br.readLine();
                if(input.equalsIgnoreCase("done"))
                {
                    break;
                }
                inputs.add(input);
            }
        }
        catch(IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }

        return inputs;
    }
}
