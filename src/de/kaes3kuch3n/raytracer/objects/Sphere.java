package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Material;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class Sphere extends Quadric {

    public Sphere(double radius, Material material) {
        super(new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, -radius}
        }), material);
    }
}
