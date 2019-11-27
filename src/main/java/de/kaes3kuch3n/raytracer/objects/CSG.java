package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.CSGOperator;
import de.kaes3kuch3n.raytracer.utilities.Ray;

import java.util.*;

public class CSG extends Quadric {
    private Quadric first;
    private Quadric second;
    private CSGOperator operator;

    /**
     * Used for excluding the first key of a tree-map
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static Double SMALL_VALUE = 0.0000000001d;


    public CSG(Quadric quadric) {
        first = quadric;
    }

    public <T extends Quadric> CSG(T first, T second, CSGOperator operator) {
        this.first = first;
        this.second = second;
        this.operator = operator;
    }

    public Ray.Hit getFirstRayHit(Ray ray) {
        SortedMap<Double, Ray.Hit> hits = getRayHitsRecursive(ray);
        if (hits == null || hits.isEmpty())
            return null;
        return hits.get(hits.firstKey());
    }

    private SortedMap<Double, Ray.Hit> getRayHitsRecursive(Ray ray) {
        SortedMap<Double, Ray.Hit> firstHits = getRayHitsForCSG(first, ray);
        SortedMap<Double, Ray.Hit> secondHits = getRayHitsForCSG(second, ray);

        if (first == null)
            return secondHits;
        else if (second == null)
            return firstHits;

        SortedMap<Double, Ray.Hit> allHits = new TreeMap<>();

        switch (operator) {
            case COMBINE:
                if (firstHits.isEmpty() || secondHits.isEmpty()) {
                    allHits.putAll(firstHits);
                    allHits.putAll(secondHits);
                    return allHits;
                }
                SortedMap<Double, Ray.Hit> firstHitsTemp = new TreeMap<>(firstHits);
                SortedMap<Double, Ray.Hit> secondHitsTemp = new TreeMap<>(secondHits);

                secondHitsTemp.subMap(firstHits.firstKey() + SMALL_VALUE, firstHits.lastKey()).clear();
                firstHitsTemp.subMap(secondHits.firstKey(), secondHits.lastKey()).clear();

                allHits.putAll(secondHitsTemp);
                allHits.putAll(firstHitsTemp);


                break;
            case SUBTRACT:
                //First missed? Second missed? First before second hit?
                if (firstHits.isEmpty() || secondHits.isEmpty() || firstHits.firstKey() < secondHits.firstKey())
                    return firstHits;
                //Going through the object
                if (firstHits.lastKey() < secondHits.lastKey())
                    return null;
                //Newly created hits (inside the object)
                SortedMap<Double, Ray.Hit> temp = firstHits.subMap(secondHits.firstKey(), secondHits.lastKey());
                Set<Quadric> insideQuadrics = new HashSet<>();
                for (Ray.Hit hit : temp.values()) {
                    if (!insideQuadrics.add(hit.quadric))
                        insideQuadrics.remove(hit.quadric);
                }

                if (!insideQuadrics.isEmpty()) {
                    SortedMap<Double, Ray.Hit> newHits = secondHits.tailMap(firstHits.firstKey());
                    newHits.forEach((distance, hit) -> hit.invertNormal());
                    allHits.putAll(newHits);
                }

                firstHits.subMap(secondHits.firstKey(), secondHits.lastKey()).clear();
                allHits.putAll(firstHits);
                break;
            case INTERSECT:
                if (firstHits.isEmpty() || secondHits.isEmpty())
                    return null;
                if (firstHits.lastKey() < secondHits.firstKey() || secondHits.lastKey() < firstHits.firstKey())
                    return null;

                temp = firstHits.headMap(secondHits.lastKey());
                List<Ray.Hit> hitsWithFirst = new ArrayList<>();
                for (Ray.Hit hit : temp.values()) {
                    if (hitsWithFirst.contains(hit) && hit.distance < secondHits.firstKey())
                        hitsWithFirst.remove(hit);
                    else
                        hitsWithFirst.add(hit);
                }
                temp = secondHits.headMap(firstHits.lastKey());
                List<Ray.Hit> hitsWithSecond = new ArrayList<>();
                for (Ray.Hit hit : temp.values()) {
                    if (hitsWithSecond.contains(hit) && hit.distance < firstHits.firstKey())
                        hitsWithSecond.remove(hit);
                    else
                        hitsWithSecond.add(hit);
                }
                if(hitsWithSecond.size() > 0)
                    allHits.putAll(firstHits.subMap(hitsWithSecond.get(0).distance, secondHits.lastKey()));
                if(hitsWithFirst.size() > 0)
                    allHits.putAll(secondHits.subMap(hitsWithFirst.get(0).distance, firstHits.lastKey()));

                break;
        }

        return allHits;
    }

    /**
     * Returns all hits or an empty map if nothing hiz
     *
     * @param obj The current quadric-type object
     * @param ray The current ray
     * @return A map with all (or none) hits
     */
    private <T extends Quadric> SortedMap<Double, Ray.Hit> getRayHitsForCSG(T obj, Ray ray) {
        if (obj == null) return null;
        SortedMap<Double, Ray.Hit> hits = new TreeMap<>();
        SortedMap<Double, Ray.Hit> hitsTemp;
        if (obj instanceof CSG)
            hitsTemp = ((CSG) obj).getRayHitsRecursive(ray);
        else
            hitsTemp = obj.getRayHit(ray);

        if (hitsTemp != null)
            hits.putAll(hitsTemp);
        return hits;
    }
}
