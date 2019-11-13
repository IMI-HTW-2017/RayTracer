package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Matrix4;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

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

    public void move(float x, float y, float z) {
        q.translate(x, y, z);
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

    public Ray.Hit getRayhit(Ray ray) {
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
        // Only first hit
        double distance = Math.min(cc / k, k / aa);
        // Negative distance? Nothing hit
        if (distance < 0)
            return null;

        Vector3 position = new Vector3(p.x + distance * v.x, p.y + distance * v.y, p.z + distance * v.z);
        return new Ray.Hit(position, distance);
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
