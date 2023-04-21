package com.google.maps;

import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

public class DrivingDistance
{
    private final GeoApiContext apiContext = ClientDeliveryController.apiContext;

    public double CalculateDrivingDistance(LatLng origin, LatLng destination)
    {
        double drivingDistance = -1.0;
        DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(apiContext)
                .origins(origin)
                .destinations(destination)
                .units(Unit.IMPERIAL)
                .mode(TravelMode.DRIVING);
        try
        {
            DistanceMatrix apiResult = request.await();

            assertNotNull(apiResult);

            DistanceMatrixElement resultElement = apiResult.rows[0].elements[0];

            drivingDistance = resultElement.distance.inMeters * 0.000621371;

        } catch(IOException | InterruptedException | ApiException ex)
        {
            ex.printStackTrace();
        }

        return drivingDistance;
    }
}