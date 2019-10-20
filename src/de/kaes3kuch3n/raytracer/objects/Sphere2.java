package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.Camera;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class Sphere2 extends RCObject {
    private float radius;

    public Sphere2(Vector3 position, float radius) {
        super(position);
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public double getRayDistanceToCamera(Ray ray, Camera camera) {
        Vector3 direction = ray.getDirection();
        Vector3 cameraPos = camera.getPosition();
        double a = Math.pow(direction.x, 2) + Math.pow(direction.y, 2) + Math.pow(direction.z, 2);
        double b = 2 * (direction.x * position.x + direction.y * position.y + direction.z * position.z
                - direction.x * cameraPos.x - direction.y *cameraPos.y - direction.z * cameraPos.z);
        double c = position.x * position.x + position.y * position.y + position.z * position.z
                - 2 * position.x * cameraPos.x - 2 * position.y * cameraPos.y - 2 * position.z * cameraPos.z
                + cameraPos.x * cameraPos.x + cameraPos.y * cameraPos.y + cameraPos.z * cameraPos.z
                - radius * radius;

        if(a == 0)
            return -1;

        double radicand = b * b - 4 * a * c;
        if(radicand < 0)
            return -1;

        double k = (-b - (b < 0 ? -1 : 1 ) * Math.sqrt(radicand)) / 2.0;

        return Math.min(c / k, k / a);
    }
}
