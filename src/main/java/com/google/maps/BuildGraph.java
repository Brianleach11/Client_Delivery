package com.google.maps;

import com.google.maps.model.LatLng;
//import io.opencensus.trace.Link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Vertex implements Comparable<Vertex>
{
    public Integer ID;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(Integer id) { ID = id; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}
class Edge
{
    public final Vertex target;
    public final double weight;
    public Edge(Vertex argTarget, double argWeight)
    { target = argTarget; weight = argWeight; }
}

public class BuildGraph
{
    LinkedList<LinkedList<Double>> graph;
    Vertex[] vertices;
    DrivingDistance findDistance = new DrivingDistance();


    public BuildGraph(ArrayList<LatLng> points)
    {
        //the points arrayList contains coordinate pairs of longitudes and latitudse
        vertices = new Vertex[points.size()];
        graph = new LinkedList<>();

        for(int i = 0; i < points.size(); i++)
        {
            vertices[i] = new Vertex(i);
            vertices[i].ID = i;
            vertices[i].adjacencies = new Edge[points.size()];
            graph.add(new LinkedList<>());
            for(int j = 0; j < points.size(); j++)
            {
                if(i == j) {
                    graph.get(i).add(0.0);
                    continue;
                }

                double distance = findDistance.CalculateDrivingDistance(points.get(i), points.get(j));
                graph.get(i).add(distance);
            }
        }

        for(int i = 0; i < points.size(); i++)
        {
            for(int j = 0; j < points.size(); j++)
            {
                if(i == j)
                {
                    vertices[i].adjacencies[j] = new Edge(vertices[i], Double.POSITIVE_INFINITY);
                    continue;
                }
                double distance = findDistance.CalculateDrivingDistance(points.get(i), points.get(j));
                vertices[i].adjacencies[j] = new Edge(vertices[j] , distance);
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target)
    {
        List<Vertex> path = new ArrayList<>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }
}
