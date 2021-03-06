package de.kaes3kuch3n.raytracer.utilities;

import de.kaes3kuch3n.raytracer.objects.Quadric;

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
     *
     * @return The ray's origin vector
     */
    public Vector3 getOrigin() {
        return origin;
    }

    /**
     * Get the ray's direction
     *
     * @return The ray's direction vector
     */
    public Vector3 getDirection() {
        return direction;
    }

    public static class Hit {
        public Ray ray;
        public Vector3 position;
        public double distance;
        public Quadric quadric;
        public boolean invertedNormal;

        public Hit(Ray ray, Vector3 position, double distance, Quadric quadric) {
            this.ray = ray;
            this.position = position;
            this.distance = distance;
            this.quadric = quadric;
        }

        public void invertNormal() {
            invertedNormal = true;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Hit && ((Hit) obj).quadric.equals(this.quadric);
        }
    }
}

