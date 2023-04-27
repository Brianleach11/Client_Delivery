package com.google.maps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.LatLng;


public class PathSolver
{
    private List<LatLng> tour;
    private DrivingDistance distance;

    public PathSolver() {
        this.tour = new ArrayList<LatLng>();
        this.distance = new DrivingDistance();
    }

    public void solve(List<LatLng> latLngList) throws Exception {
        // Define your starting point and initialize your tour
        LatLng start = latLngList.get(0);
        tour.add(start);

        Set<LatLng> unvisitedPoints = new HashSet<LatLng>(latLngList);
        unvisitedPoints.remove(start);

        // Build the greedy tour by iteratively selecting the nearest unvisited point
        while (!unvisitedPoints.isEmpty()) {
            LatLng current = tour.get(tour.size() - 1);
            LatLng nearest = nearestUnvisitedPoint(current, unvisitedPoints);
            tour.add(nearest);
            unvisitedPoints.remove(nearest);
        }

        // Optimize the tour with the 2-opt algorithm
        boolean improvement = true;
        while (improvement) {
            improvement = false;
            for (int i = 1; i < tour.size() - 2; i++) {
                for (int j = i + 1; j < tour.size(); j++) {
                    if (j - i == 1)
                        continue;
                    List<LatLng> newTour = twoOptSwap(tour, i, j);
                    double newLength = tourLength(newTour);
                    if (newLength < tourLength(tour)) {
                        tour = newTour;
                        improvement = true;
                    }
                }
                if (improvement)
                    break;
            }
        }
    }

    //this works
    private LatLng nearestUnvisitedPoint(LatLng point, Set<LatLng> unvisitedPoints) throws Exception {
        LatLng nearestPoint = null;
        double nearestDistance = Long.MAX_VALUE;

        for (LatLng candidate : unvisitedPoints) {
            double candidateDistance = distance.CalculateDrivingDistance(point, candidate);
            if (candidateDistance < nearestDistance) {
                nearestPoint = candidate;
                nearestDistance = candidateDistance;
            }
        }

        return nearestPoint;
    }

    //not sure
    private List<LatLng> twoOptSwap(List<LatLng> tour, int i, int j) {
        List<LatLng> newTour = new ArrayList<LatLng>();

        // Add tour[0] to tour[i-1] to the new tour
        for (int k = 0; k <= i - 1; k++) {
            newTour.add(tour.get(k));
        }

        // Add tour[j] to tour[i] to the new tour in reverse order
        for (int k = j; k >= i; k--) {
            newTour.add(tour.get(k));
        }

        // Add tour[j+1] to the end of the new tour
        for (int k = j + 1; k < tour.size(); k++) {
            newTour.add(tour.get(k));
        }

        // If the swap removed a node from the tour, add it back at the end
        if (newTour.size() < tour.size()) {
            newTour.add(tour.get(0));
        }

        return newTour;
    }

    //this works
    private double tourLength(List<LatLng> tour) throws Exception {
        double length = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            length += distance.CalculateDrivingDistance(tour.get(i), tour.get(i + 1));
        }
        length += distance.CalculateDrivingDistance(tour.get(tour.size() - 1), tour.get(0));
        return length;
    }

    public List<LatLng> getTour() {
        return tour;
    }

}
