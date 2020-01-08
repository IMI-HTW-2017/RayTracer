package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

public class Light {
    private Vector3 position;
    private Vector3 color;
    private double intensity;

    public Light(Vector3 position, Color color, double intensity) {
        this.position = position;
        this.color = new Vector3(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
        this.intensity = intensity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }
}
