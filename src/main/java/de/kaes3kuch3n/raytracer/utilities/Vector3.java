package de.kaes3kuch3n.raytracer.utilities;

public class Vector3 {
    public double x;
    public double y;
    public double z;

    /**
     * Create a new three-dimensional vector
     *
     * @param x The x-coordinate of the vector
     * @param y The y-coordinate of the vector
     * @param z The z-coordinate of the vector
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", x, y, z);
    }

    /**
     * Adds two vectors
     *
     * @param first  The first addend
     * @param second The second addend
     * @return The sum of the two vectors
     */
    public static Vector3 add(Vector3 first, Vector3 second) {
        return new Vector3(first.x + second.x, first.y + second.y, first.z + second.z);
    }

    /**
     * Subtracts two vectors
     *
     * @param first  The minuend
     * @param second The subtrahend
     * @return The difference between the two vectors
     */
    public static Vector3 subtract(Vector3 first, Vector3 second) {
        return new Vector3(first.x - second.x, first.y - second.y, first.z - second.z);
    }

    /**
     * Multiplies a vector with a factor
     *
     * @param factor The factor to multiply the vector with
     * @return The product vector of the vector and the factor
     */
    public Vector3 multiply(double factor) {
        return new Vector3(x * factor, y * factor, z * factor);
    }

    /**
     * Divides a vector using the given divisor
     *
     * @param divisor The divisor to divide the vector with
     * @return The product vector of the vector and the factor
     */
    public Vector3 divide(double divisor) {
        return new Vector3(x / divisor, y / divisor, z / divisor);
    }

    /**
     * Calculates the dot product of two vectors
     *
     * @param first  The first vector
     * @param second The second vector
     * @return The dot product of the two vectors
     */
    public static double dot(Vector3 first, Vector3 second) {
        return first.x * second.x + first.y * second.y + first.z * second.z;
    }

    /**
     * Calculates the cross product of two vectors
     *
     * @param first  The first vector
     * @param second The second vector
     * @return The cross product of the two vectors
     */
    public static Vector3 cross(Vector3 first, Vector3 second) {
        double x = first.y * second.z - first.z * second.y;
        double y = first.z * second.x - first.x * second.z;
        double z = first.x * second.y - first.y * second.x;
        return new Vector3(x, y, z);
    }

    /**
     * Inverts a vector
     *
     * @return The inverted vector
     */
    public Vector3 inverted() {
        return new Vector3(-x, -y, -z);
    }

    /**
     * Normalizes a vector to a length of one
     *
     * @param vector The vector to normalize
     * @return The normalized vector
     */
    public static Vector3 normalize(Vector3 vector) {
        double magnitude = magnitude(vector);
        return new Vector3(vector.x / magnitude, vector.y / magnitude, vector.z / magnitude);
    }

    /**
     * Gets the magnitude of a vector
     *
     * @param vector The vector to get the magnitude for
     * @return The magnitude of the vector
     */
    public static double magnitude(Vector3 vector) {
        return Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
    }

    /**
     * Gets the normalized version of a vector
     *
     * @return The normalized vector
     */
    public Vector3 normalized() {
        return normalize(this);
    }
}
