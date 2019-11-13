package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Operator;
import de.kaes3kuch3n.raytracer.utilities.Ray;

import java.awt.*;

public class CSG extends Quadric {
    private CSG second;
    private Operator operator;

    public CSG(float a, float b, float c, float d, float e, float f, float g, float h, float i, float j, Color color) {
        super(a, b, c, d, e, f, g, h, i, j, color);
    }

    public void addCSG(CSG second, Operator operator) {
        this.second = second;
        this.operator = operator;
    }

    @Override
    public Ray.Hit getFirstRayhit(Ray ray) {
        Ray.Hit[] hitsWithFirstCSG = super.getRayhits(ray);
        //No second CSG, no need to get the "lower" CSG
        if (second == null)
            return hitsWithFirstCSG[0];
        Ray.Hit[] hitsWithSecondCSG = second.getRayhits(ray);
        //Nothing hit, no need to calculate a new CSG
        if (hitsWithFirstCSG == null && hitsWithSecondCSG == null)
            return null;

        switch (operator) {
            case COMBINE:
                if (hitsWithFirstCSG == null)
                    return hitsWithSecondCSG[0];
                else if (hitsWithSecondCSG == null)
                    return hitsWithFirstCSG[0];
                else
                    return Ray.Hit.Min(hitsWithFirstCSG[0], hitsWithSecondCSG[0]);
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
    }
}