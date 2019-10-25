package de.kaes3kuch3n.raytracer.utilities;

public class Ray {
    private Vector3 origin;
    private Vector3 direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public Vector3 getDirection() {
        return direction;
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

