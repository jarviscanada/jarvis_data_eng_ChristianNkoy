package ca.jrvs.apps.twitter.model;

public class Coordinates {
    private double [] coordinates;
    private String type;

    public Coordinates(double[] coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }
}
