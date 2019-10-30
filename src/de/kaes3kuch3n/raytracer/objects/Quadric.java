package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Matrix4;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class Quadric {

    private Matrix4 q;

    public Quadric(float a, float b, float c, float d, float e, float f, float g, float h, float i, float j) {
        q = new Matrix4(new float[]{
                a, d, e, g,
                d, b, f, h,
                e, f, c, i,
                g, h, i, j
        });
    }

    public Ray.Hit getRayhit(Ray ray) {
        Vector3 direction = ray.getDirection();
        Vector3 origin = ray.getOrigin();
        float[] values = q.getValuesAsArray();
        float a = values[0], b = values[5], c = values[10], d = values[1], e = values[2], f = values[6], g = values[3],
                h = values[7], i = values[11], j = values[15];
        double a = values[0] + values[5] + values[10] + 2 * values[1];
        double b = :
        double c = ;
        return null;
    }
}
