package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

public class Sphere {
    private Vector3 position;
    private double radius;
    private Color color;

    public Sphere(double x, double y, double z, double radius, Color color) {
        this(new Vector3(x, y, z), radius, color);
    }

    @Override
    public String toString() {
        return this.color.toString();
    }

    public Sphere(Vector3 position, double radius, Color color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getColorRatio() {
        return new Vector3(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
    }

    public Ray.Hit getRayHit(Ray ray) {
        Vector3 rayOrigin = ray.getOrigin();
        Vector3 rayDirection = ray.getDirection();

        double a = Math.pow(rayDirection.x, 2) + Math.pow(rayDirection.y, 2) + Math.pow(rayDirection.z, 2);
        double b = 2 * (rayOrigin.x * rayDirection.x + rayOrigin.y * rayDirection.y + rayOrigin.z * rayDirection.z
                - rayDirection.x * position.x - rayDirection.y * position.y - rayDirection.z * position.z);
        double c = rayOrigin.x * rayOrigin.x + rayOrigin.y * rayOrigin.y + rayOrigin.z * rayOrigin.z
                - 2 * rayOrigin.x * position.x - 2 * rayOrigin.y * position.y - 2 * rayOrigin.z * position.z
                + position.x * position.x + position.y * position.y + position.z * position.z
                - radius * radius;

        if (a == 0)
            return null;

        //Edge-case (tangent)
        double radicand = b * b - 4 * a * c;
        if (radicand < 0)
            return null;

        double k = (-b - (b < 0 ? -1 : 1) * Math.sqrt(radicand)) / 2.0;
        //Only first hit
        double distance = Math.min(c / k, k / a);
        //Negative distance? Nothing hit
        if (distance < 0)
            return null;

        Vector3 position = new Vector3(rayOrigin.x + distance * rayDirection.x, rayOrigin.y + distance * rayDirection.y, rayOrigin.z + distance * rayDirection.z);
        return new Ray.Hit(position, distance);
    }
}
