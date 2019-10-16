package de.kaes3kuch3n.raytracer.utilities;

public class Vector3 {
    public double x;
    public double y;
    public double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 add(Vector3 first, Vector3 second) {
        return new Vector3(first.x + second.x, first.y + second.y, first.z + second.z);
    }
}
