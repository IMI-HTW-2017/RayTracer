package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Matrix4;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class Quadric {

    private Matrix4 q;
    private float a, b, c, d, e, f, g, h, i, j;
    private Color color;

    public Quadric(float a, float b, float c, float d, float e, float f, float g, float h, float i, float j, Color color) {
        q = new Matrix4(new float[]{
                a, d, e, g,
                d, b, f, h,
                e, f, c, i,
                g, h, i, j
        });
        this.color = color;
        updateValues();
    }

    private void updateValues() {
        float[] values = q.getValuesAsArray();
        a = values[0];
        b = values[5];
        c = values[10];
        d = values[1];
        e = values[2];
        f = values[6];
        g = values[3];
        h = values[7];
        i = values[11];
        j = values[15];
    }

    public Ray.Hit getFirstRayHit(Ray ray) {
        SortedMap hits = getRayHits(ray);
        return (Ray.Hit) hits.get(hits.firstKey());
    }

    protected SortedMap getRayHits(Ray ray) {
        Vector3 v = ray.getDirection();
        Vector3 p = ray.getOrigin();

        double aa = a * v.x * v.x + b * v.y * v.y + c * v.z * v.z + 2 * d * v.x * v.y + 2 * e * v.x * v.z + 2 * f * v.y * v.z;
        double bb = 2 * (a * p.x * v.x + b * p.y * v.y + c * p.z * v.z
                + d * p.x * v.y + d * p.y * v.x + e * p.x * v.z + e * p.z * v.x + f * p.y * v.z + f * p.z * v.y
                + g * v.x + h * v.y + i * v.z);
        double cc = a * p.x * p.x + b * p.y * p.y + c * p.z * p.z + 2 * d * p.x * p.y + 2 * e * p.x * p.z + 2 * f * p.y * p.z
                + 2 * g * p.x + 2 * h * p.y + 2 * i * p.z + j;

        if (aa == 0)
            return null;

        // Edge-case (tangent)
        double radicand = bb * bb - 4 * aa * cc;
        if (radicand < 0)
            return null;


        double k = (-bb - (bb < 0 ? -1 : 1) * Math.sqrt(radicand)) / 2.0;
        Double firstDistance = cc / k;
        Double secondDistance = k / aa;
        //Quadric in camera
        if (firstDistance < 0 || secondDistance < 0)
            return null;

        if (firstDistance > secondDistance) {
            Double temp = firstDistance;
            firstDistance = secondDistance;
            secondDistance = temp;
        }
        SortedMap<Double, Ray.Hit> hits = new TreeMap<>();
        hits.put(firstDistance, new Ray.Hit(new Vector3(p.x + firstDistance * v.x, p.y + firstDistance * v.y, p.z + firstDistance * v.z), firstDistance, this));
        hits.put(secondDistance, new Ray.Hit(new Vector3(p.x + secondDistance * v.x, p.y + secondDistance * v.y, p.z + secondDistance * v.z), secondDistance, this));
        return hits;
    }


    public Vector3 getNormalVector(Vector3 point) {
        return new Vector3(
                a * point.x + d * point.y + e * point.z + g,
                b * point.y + d * point.x + f * point.z + h,
                c * point.z + e * point.x + f * point.y + i
        ).normalized();
    }

    public Vector3 getColorRatio() {
        return new Vector3(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
    }
}
