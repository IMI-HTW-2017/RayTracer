package de.kaes3kuch3n.raytracer.objects;

import java.awt.*;

public class Sphere extends Quadric {

    public Sphere(float radius, Color color) {
        super(1, 1, 1, 0, 0, 0, 0, 0, 0, -radius, color);
    }
}
