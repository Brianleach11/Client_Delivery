package com.google.maps;

import com.google.gson.Gson;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.Assert.assertNotNull;

@SpringBootApplication
@RestController
public class ClientDeliveryController
{
    public static void main(String[] args) {
        SpringApplication.run(ClientDeliveryController.class, args);
    }
    public static final GeoApiContext apiContext = new BuildGeoApiContext().BuildContext();
    private final static ThreadingApisForCoords threadingApisForCoords = new ThreadingApisForCoords();
    private final static ThreadingApisForAddrs threadingApisForAddrs = new ThreadingApisForAddrs();

    @PostMapping("/optimize-route")
    public String optimizeRoute(@RequestBody List<String> inputAddresses)
    {
        List<LatLng> latLngs = new ArrayList<>();
        try
        {
            for (String input: inputAddresses)
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

        PathSolver newSolution = new PathSolver();
        try
        {
            newSolution.solve(latLngs);
            latLngs = newSolution.getTour();
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

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

        addresses.add(inputAddresses.get(0));

        assertNotNull(addresses);

        for(String addr : addresses)
        {
            System.out.println("Address:");
            System.out.println(addr);
        }

        apiContext.shutdown();

        Gson gson = new Gson();
        String optimizedRoute = gson.toJson(latLngs);

        return optimizedRoute;
    }
}
