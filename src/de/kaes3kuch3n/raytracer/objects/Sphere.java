package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

public class Sphere {
    private Vector3 position;
    private double radius;
    private Color color;

    public Sphere(double x, double y, double z, double radius, Color color) {
        this(new Vector3(x, y, z), radius, color);
    }

    public Sphere(Vector3 position, double radius, Color color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
    }

    public Vector3 getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public Vector3 getColorRatio() {
        return new Vector3(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
    }
}
