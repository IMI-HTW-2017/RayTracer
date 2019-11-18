package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Operator;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import org.apache.commons.math3.linear.RealMatrix;

import java.awt.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class CSG extends Quadric {
    private Quadric first;
    private Quadric second;
    private Operator operator;


    public CSG(RealMatrix q, Color color) {
        super(q, color);
    }

    public <T extends Quadric> CSG(T first, T second, Operator operator) {
        this.first = first;
        this.second = second;
        this.operator = operator;
    }
/*
    public void combine(CSG second) {
        addCSG(second, Operator.COMBINE);
    }

    public void subtract(CSG second) {
        addCSG(second, Operator.SUBTRACT);
    }

    public void intersect(CSG second) {
        addCSG(second, Operator.INTERSECT);
    }
*/
    public Ray.Hit getFirstRayHit(Ray ray) {
        SortedMap<Double, Ray.Hit> hits = getRayHitsRecursive(ray);
        if(hits == null || hits.isEmpty())
            return null;
        return hits.get(hits.firstKey());
    }

    private SortedMap<Double, Ray.Hit> getRayHitsRecursive(Ray ray) {
        SortedMap<Double, Ray.Hit> firstHits = getRayHitsForCSG(first, ray);
        //No second CSG, no need to get the "lower" CSG
        if (second == null)
            return firstHits;

        //Add all for second
        SortedMap<Double, Ray.Hit> secondHits = getRayHitsForCSG(second, ray);

        switch (operator) {
            case COMBINE:
                //Second missed?
                if (secondHits.isEmpty())
                    return firstHits;
                firstHits.putAll(secondHits);
                break;
            case SUBTRACT:
                //Second missed?
                if (secondHits.isEmpty())
                    return firstHits;
                if(firstHits.isEmpty() || firstHits.firstKey() < secondHits.firstKey())
                    return firstHits;
                if(firstHits.lastKey() < secondHits.lastKey())
                    return null;

                SortedMap<Double, Ray.Hit> newHits = secondHits.tailMap(firstHits.firstKey());
                firstHits.subMap(secondHits.firstKey(), secondHits.lastKey()).clear();
                newHits.forEach((distance, hit) -> hit.invertNormal());
                firstHits.putAll(newHits);
                break;
            case INTERSECT:
                if(firstHits.isEmpty() || secondHits.isEmpty())
                    return null;
                if(firstHits.lastKey() < secondHits.firstKey() || secondHits.lastKey() < firstHits.firstKey())
                    return null;
                SortedMap<Double, Ray.Hit> newMapFirst = firstHits;
                if(firstHits.firstKey() < secondHits.firstKey()) {
                    newMapFirst.subMap(secondHits.firstKey(), firstHits.lastKey());
                    secondHits.subMap(secondHits.firstKey(), firstHits.lastKey());
                }
                else {
                    newMapFirst.subMap(firstHits.firstKey(), secondHits.lastKey());
                    secondHits.subMap(firstHits.firstKey(), secondHits.lastKey());
                }
                firstHits = newMapFirst;
                firstHits.putAll(secondHits);
                break;
        }
        return firstHits;
    }

    private SortedMap<Double, Ray.Hit> getRayHitsForCSG(Quadric obj, Ray ray) {
        SortedMap<Double, Ray.Hit> hits = new TreeMap<>();
        SortedMap<Double, Ray.Hit> hitsTemp;
        if(obj instanceof CSG)
            hitsTemp = ((CSG) obj).getRayHitsRecursive(ray);
        else
            hitsTemp = obj.getRayHit(ray);
        if(hitsTemp != null)
            hits.putAll(hitsTemp);
        return hits;
    }
}
