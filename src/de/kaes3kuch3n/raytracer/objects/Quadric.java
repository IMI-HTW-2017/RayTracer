package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Matrix4;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class Quadric {

    private Matrix4 q;
    private float a, b, c, d, e, f, g, h, i, j;


    public Quadric(float a, float b, float c, float d, float e, float f, float g, float h, float i, float j) {
        q = new Matrix4(new float[]{
                a, d, e, g,
                d, b, f, h,
                e, f, c, i,
                g, h, i, j
        });
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
        float[] values = q.getValuesAsArray();
        float a = values[0], b = values[5], c = values[10], d = values[1], e = values[2], f = values[6], g = values[3],
                h = values[7], i = values[11], j = values[15];

        double aa = a + b + c + 2 * d * v.x * v.y + 2 * e * v.x * v.z + 2 * f * v.y * v.z;
        double bb = 2 * (a * p.x * v.x + b * p.y * v.y * c * p.z * v.z
                + d * p.x * v.y + d * p.y * v.x + e * p.x * v.z + e * p.z * v.x + f * p.y * v.z + f * p.z * v.y
                + g * v.x + h * v.y + i * v.z);
        double cc = a * p.x * p.x + b * p.y * p.y + c * p.z * p.z + v.x * v.x + v.y * v.y + v.z * v.z
                + 2 * d * p.x * p.y + 2 * e * p.x * p.z + 2 * f * p.y * p.z
                + 2 * g * p.x + 2 * h * p.y + 2 * i * p.z + j;

        if (aa == 0)
            return null;

        //Edge-case (tangent)
        double radicand = bb * bb - 4 * aa * cc;
        if (radicand < 0)
            return null;

        double k = (-bb - (bb < 0 ? -1 : 1) * Math.sqrt(radicand)) / 2.0;
        //Only first hit
        double distance = Math.min(cc / k, k / aa);
        //Negative distance? Nothing hit
        if (distance < 0)
            return null;

        return null;
    }

    public Vector3 getNormalVector(Vector3 point) {
        return new Vector3(a * point.x + d * point.y + e * point.z + g,
                b * point.y + d * point.x + f * point.z + h,
                c * point.z + e * point.x + f * point.y + i).normalized();
    }

}
