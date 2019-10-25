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

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", x, y, z);
    }

    public static Vector3 add(Vector3 first, Vector3 second) {
        return new Vector3(first.x + second.x, first.y + second.y, first.z + second.z);
    }

    public static Vector3 subtract(Vector3 first, Vector3 second) {
        return new Vector3(first.x - second.x, first.y - second.y, first.z - second.z);
    }

    public Vector3 multiply(double factor) {
        return new Vector3(x * factor, y * factor, z * factor);
    }

    public static double dot(Vector3 first, Vector3 second) {
        return first.x * second.x + first.y * second.y + first.z * second.z;
    }

    public static Vector3 cross(Vector3 first, Vector3 second) {
        double x = first.y * second.z - first.z * second.y;
        double y = first.z * second.x - first.x * second.z;
        double z = first.x * second.y - first.y * second.x;
        return new Vector3(x, y, z);
    }

    public Vector3 inverted() {
        return new Vector3(-x, -y, -z);
    }

    public static Vector3 normalize(Vector3 vector) {
        double magnitude = magnitude(vector);
        return new Vector3(vector.x / magnitude, vector.y / magnitude, vector.z / magnitude);
    }

    public static double magnitude(Vector3 vector) {
        return Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
    }

    public Vector3 normalize() {
        return normalize(this);
    }
}
