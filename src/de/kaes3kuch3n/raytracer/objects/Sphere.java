package de.kaes3kuch3n.raytracer.objects;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.awt.*;

public class Sphere extends Quadric {

    public Sphere(double radius, Color color) {
        super(new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, -radius}
        }), color);
    }
}
