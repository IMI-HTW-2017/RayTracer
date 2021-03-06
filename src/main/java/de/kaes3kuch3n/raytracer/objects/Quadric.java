package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Material;
import de.kaes3kuch3n.raytracer.utilities.MatrixHelper;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.SortedMap;
import java.util.TreeMap;

public class Quadric {

    public RealMatrix q;
    private double a, b, c, d, e, f, g, h, i, j;
    private Material material;

    public Quadric(RealMatrix q, Material material) {
        this.q = q;
        this.material = material;
        updateValues();
    }

    public Quadric() {
    }

    public Quadric translate(double x, double y, double z) {
        return transform(MatrixHelper.createTranslation(x, y, z));
    }

    public Quadric scale(double factor) {
        return transform(MatrixHelper.createScaling(factor, factor, factor));
    }

    public Quadric scale(double x, double y, double z) {
        return transform(MatrixHelper.createScaling(x, y, z));
    }

    public Quadric rotateX(double angle) {
        return transform(MatrixHelper.createRotationX(angle));
    }

    public Quadric rotateY(double angle) {
        return transform(MatrixHelper.createRotationY(angle));
    }

    public Quadric rotateZ(double angle) {
        return transform(MatrixHelper.createRotationZ(angle));
    }

    public SortedMap<Double, Ray.Hit> getRayHit(Ray ray) {
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
        if (radicand <= 0)
            return null;


        double k = (-bb - (bb < 0 ? -1 : 1) * Math.sqrt(radicand)) / 2.0;

        double distance1 = cc / k;
        double distance2 = k / aa;
        TreeMap<Double, Ray.Hit> hits = new TreeMap<>();
        Ray.Hit firstHit = new Ray.Hit(ray, new Vector3(p.x + distance1 * v.x, p.y + distance1 * v.y, p.z + distance1 * v.z), distance1, this);
        Ray.Hit secondHit = new Ray.Hit(ray, new Vector3(p.x + distance2 * v.x, p.y + distance2 * v.y, p.z + distance2 * v.z), distance2, this);

        if (distance1 < 0 && distance2 > 0) {
            secondHit.invertNormal();
            hits.put(distance2, secondHit);
            return hits;
        }
        if (distance2 < 0 && distance1 > 0) {
            firstHit.invertNormal();
            hits.put(distance1, firstHit);
            return hits;
        }
        if (distance1 > 0)
            hits.put(distance1, firstHit);
        if (distance2 > 0)
            hits.put(distance2, secondHit);
        return hits;
    }

    public Vector3 getNormalVector(Vector3 point) {
        return new Vector3(
                a * point.x + d * point.y + e * point.z + g,
                b * point.y + d * point.x + f * point.z + h,
                c * point.z + e * point.x + f * point.y + i
        ).normalized();
    }

    public Material getMaterial() {
        return material;
    }

    private Quadric transform(RealMatrix transformationMatrix) {
        RealMatrix inverse = new LUDecomposition(transformationMatrix).getSolver().getInverse();
        q = inverse.transpose().multiply(q.multiply(inverse));
        updateValues();
        return this;
    }

    private void updateValues() {
        a = q.getEntry(0, 0);
        b = q.getEntry(1, 1);
        c = q.getEntry(2, 2);
        d = q.getEntry(1, 0);
        e = q.getEntry(2, 0);
        f = q.getEntry(2, 1);
        g = q.getEntry(3, 0);
        h = q.getEntry(3, 1);
        i = q.getEntry(3, 2);
        j = q.getEntry(3, 3);
    }
}
