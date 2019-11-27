package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Material;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class Plane extends Quadric {
    public Plane(double x, double y, double z, Material material) {
        super(new Array2DRowRealMatrix(new double[][]{
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, -0.9}
        }), material);
    }
}
