package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

public class Light {
    private Vector3 position;
    private Color color;
    private float intensity;

    public Light(Vector3 position, Color color, float intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }
}
