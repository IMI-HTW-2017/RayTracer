package de.kaes3kuch3n.raytracer.utilities;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class MatrixHelper {

    public static RealMatrix createTranslation(double x, double y, double z) {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(4);
        matrix.setEntry(0, 3, x);
        matrix.setEntry(1, 3, y);
        matrix.setEntry(2, 3, z);
        return matrix;
    }

    public static RealMatrix createScaling(double x, double y, double z) {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(4);
        matrix.setEntry(0, 0, x);
        matrix.setEntry(1, 1, y);
        matrix.setEntry(2, 2, z);
        return matrix;
    }

    public static RealMatrix createRotationX(double angle) {
        angle = Math.toRadians(angle);
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(4);
        matrix.setEntry(1, 1, Math.cos(angle));
        matrix.setEntry(1, 2, Math.sin(angle));
        matrix.setEntry(2, 1, -Math.sin(angle));
        matrix.setEntry(2, 2, Math.cos(angle));
        return matrix;
    }

    public static RealMatrix createRotationY(double angle) {
        angle = Math.toRadians(angle);
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(4);
        matrix.setEntry(0, 0, Math.cos(angle));
        matrix.setEntry(0, 2, Math.sin(angle));
        matrix.setEntry(2, 0, -Math.sin(angle));
        matrix.setEntry(2, 2, Math.cos(angle));
        return matrix;
    }

    public static RealMatrix createRotationZ(double angle) {
        angle = Math.toRadians(angle);
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(4);
        matrix.setEntry(0, 0, Math.cos(angle));
        matrix.setEntry(0, 1, Math.sin(angle));
        matrix.setEntry(1, 0, -Math.sin(angle));
        matrix.setEntry(1, 1, Math.cos(angle));
        return matrix;
    }
}
