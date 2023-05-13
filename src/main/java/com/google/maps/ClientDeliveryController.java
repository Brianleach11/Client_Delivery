package com.google.maps;

import com.google.gson.Gson;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import static org.junit.Assert.assertNotNull;

@SpringBootApplication
@RestController
public class ClientDeliveryController
{
    public static void main(String[] args) {
        SpringApplication.run(ClientDeliveryController.class, args);
    }
    public static GeoApiContext apiContext;
    private final static ThreadingApisForCoords threadingApisForCoords = new ThreadingApisForCoords();
    private final static ThreadingApisForAddrs threadingApisForAddrs = new ThreadingApisForAddrs();

    @Autowired
    private BuildGeoApiContext apiContextBuilder = new BuildGeoApiContext();

    @ExceptionHandler
    public void handleException(Exception ex) {
        // Ensure apiContext is closed even if an exception is thrown
        apiContextBuilder.close();
    }

    @PostMapping(value ="/optimize-route", consumes = "application/json", produces = "application/json")
    public String optimizeRoute(@RequestBody AddressList requestBody) {

        apiContext = apiContextBuilder.get();

        try
        {
            List<String> inputAddresses = requestBody.getStrings();
            List<LatLng> latLngs = new ArrayList<>();
            try {
                for (String input : inputAddresses) {
                    threadingApisForCoords.run(input);
                    threadingApisForCoords.join();
                    latLngs.add(threadingApisForCoords.GetResults());
                }
            } catch (InterruptedException interruptedException) {
                System.out.println("Threading for Coords");
                System.out.println(interruptedException.getMessage());
            }

            assertNotNull(latLngs);

            PathSolver newSolution = new PathSolver();
            try {
                newSolution.solve(latLngs);
                latLngs = newSolution.getTour();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            latLngs.add(latLngs.get(0));

            /*
            ArrayList<String> addresses = new ArrayList<>();

            try {
                for (LatLng latLng : latLngs) {
                    System.out.println("Latitude: " + latLng.lat + " Longitude: " + latLng.lng);
                    threadingApisForAddrs.run(latLng);
                    threadingApisForAddrs.join();
                    addresses.add(threadingApisForAddrs.GetResults());
                }
            } catch (InterruptedException interruptedException) {
                System.out.println("Threading for Addrs");
                System.out.println(interruptedException.getMessage());
            }

            addresses.add(inputAddresses.get(0));

            assertNotNull(addresses);

            for (String addr : addresses) {
                System.out.println("Address:");
                System.out.println(addr);
            }

             */

            Gson gson = new Gson();
            String optimizedRoute = gson.toJson(latLngs);

            return optimizedRoute;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "Error occured: " + ex.getMessage();
        }

    }
}