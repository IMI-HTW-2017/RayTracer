package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Operator;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import org.apache.commons.math3.linear.RealMatrix;

import java.awt.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class CSG extends Quadric {
    private CSG second;
    public Operator operator;


    public CSG(RealMatrix q, Color color) {
        super(q, color);
    }

    public void combine(CSG second) {
        addCSG(second, Operator.COMBINE);
    }

    public void subtract(CSG second) {
        addCSG(second, Operator.SUBTRACT);
    }

    public void intersect(CSG second) {
        addCSG(second, Operator.INTERSECT);
    }

    public Ray.Hit getFirstRayHit(Ray ray) {
        SortedMap<Double, Ray.Hit> hits = getRayHitsRecursive(ray);
        if(hits == null || hits.isEmpty())
            return null;
        return hits.get(hits.firstKey());
    }

    private SortedMap<Double, Ray.Hit> getRayHitsRecursive(Ray ray) {
        SortedMap<Double, Ray.Hit> firstHits = new TreeMap<>();
        SortedMap<Double, Ray.Hit> hit1 = super.getRayHit(ray);
        if(hit1 != null)
            firstHits.putAll(hit1);
        //No second CSG, no need to get the "lower" CSG
        if (second == null)
            return firstHits;

        SortedMap<Double, Ray.Hit> secondHits = second.getRayHitsRecursive(ray);

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

    private void addCSG(CSG second, Operator operator) {
        this.second = second;
        this.operator = operator;
    }
    /*
        case INTERSECTION:
            if (hitsWithFirstCSG == null || hitsWithSecondCSG == null)
                return null;
            double firstEnter, firstExit, secondEnter, secondExit;
            firstEnter = hitsWithFirstCSG[0].distance;
            firstExit = hitsWithFirstCSG[1].distance;
            secondEnter = hitsWithFirstCSG[0].distance;
            secondExit = hitsWithSecondCSG[1].distance;
            if (firstExit < secondEnter || secondExit < firstEnter)
                return null;
            else if (firstEnter < secondEnter)
                return hitsWithSecondCSG[0];
            else
                return hitsWithFirstCSG[0];
        case DIFFERENCE:
            if (hitsWithFirstCSG == null)
                return null;
            else if (hitsWithSecondCSG == null)
                return hitsWithFirstCSG[0];
            firstEnter = hitsWithFirstCSG[0].distance;
            firstExit = hitsWithFirstCSG[1].distance;
            secondEnter = hitsWithFirstCSG[0].distance;
            secondExit = hitsWithSecondCSG[1].distance;
            if (secondExit < firstEnter)
                return null;
            else if (firstEnter < secondEnter)
                return hitsWithFirstCSG[0];
            else
                return hitsWithSecondCSG[1];
    }
        return null;
     */
}
