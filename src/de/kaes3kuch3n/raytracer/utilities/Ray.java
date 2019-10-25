package de.kaes3kuch3n.raytracer.utilities;

import de.kaes3kuch3n.raytracer.objects.Sphere;

public class Ray {
    private Vector3 origin;

    public Vector3 getDirection() {
        return direction;
    }

    private Vector3 direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Hit getDistanceFromOrigin(Sphere sphere) {
        Vector3 spherePos = sphere.getPosition();
        double a = Math.pow(direction.x, 2) + Math.pow(direction.y, 2) + Math.pow(direction.z, 2);
        double b = 2 * (origin.x * direction.x + origin.y * direction.y + origin.z * direction.z
                - direction.x * spherePos.x - direction.y * spherePos.y - direction.z * spherePos.z);
        double c = origin.x * origin.x + origin.y * origin.y + origin.z * origin.z
                - 2 * origin.x * spherePos.x - 2 * origin.y * spherePos.y - 2 * origin.z * spherePos.z
                + spherePos.x * spherePos.x + spherePos.y * spherePos.y + spherePos.z * spherePos.z
                - sphere.getRadius() * sphere.getRadius();

        //Nothing hit
        if(a == 0)
            return null;

        //Edge-case (tangent)
        double radicand = b * b - 4 * a * c;
        if(radicand < 0)
            return null;

        double k = (-b - (b < 0 ? -1 : 1 ) * Math.sqrt(radicand)) / 2.0;
        double distance = Math.min(c / k, k / a);
        Vector3 position = new Vector3(origin.x + distance * direction.x, origin.y + distance * direction.y, origin.z + distance * direction.z);

        return new Hit(position, distance);
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

