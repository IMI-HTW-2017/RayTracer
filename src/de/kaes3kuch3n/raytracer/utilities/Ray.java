package de.kaes3kuch3n.raytracer.utilities;

public class Ray {
    private Vector3 origin;
    private Vector3 direction;

    /**
     * Create a new ray
     *
     * @param origin    The origin position vector of the ray
     * @param direction The direction vector of the ray
     */
    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    /**
     * Get the ray's origin
     * @return The ray's origin vector
     */
    public Vector3 getOrigin() {
        return origin;
    }

    /**
     * Get the ray's direction
     * @return The ray's direction vector
     */
    public Vector3 getDirection() {
        //return direction;
        return Vector3.subtract(direction, origin);
    }

    public static class Hit {
        public Vector3 position;
        public double distance;

        public Hit(Vector3 position, double distance) {
            this.position = position;
            this.distance = distance;
        }
    }
}

