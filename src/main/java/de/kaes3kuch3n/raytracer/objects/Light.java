package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.util.Random;

public class Light {
    private Vector3 position;
    private double radius;
    private Vector3 color;
    private double intensity;

    public Light(Vector3 position, double radius, Color color, double intensity) {
        this.position = position;
        this.radius = radius;
        this.color = new Vector3(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
        this.intensity = intensity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getRandomPoint() {
        double x = 0;
        double y = 0;
        double z = 0;

        while (x == 0 && y == 0 && z == 0) {
            Random random = new Random();
            x = random.nextGaussian();
            y = random.nextGaussian();
            z = random.nextGaussian();

            double magnitude = Math.sqrt(x * x + y * y + z * z);
            x /= magnitude;
            y /= magnitude;
            z /= magnitude;
        }

        return new Vector3(position.x + x * radius, position.y + y * radius, position.z + z * radius);
    }

    public Vector3 getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }
}
