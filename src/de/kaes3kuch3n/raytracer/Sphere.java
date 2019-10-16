package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class Sphere {
    private Vector3 position;
    private double radius;

    public Sphere(double x, double y, double z, double radius) {
        this(new Vector3(x, y, z), radius);
    }

    public Sphere(Vector3 position, double radius) {
        this.position = position;
        this.radius = radius;
    }

    public Vector3 getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }
}
