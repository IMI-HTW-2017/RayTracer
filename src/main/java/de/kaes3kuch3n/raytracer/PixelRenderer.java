package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.util.concurrent.Callable;

public class PixelRenderer implements Callable<PixelRenderer.Result> {
    private final Scene scene;
    private final int x;
    private final int y;
    private final double stepSizeX;
    private final double stepSizeY;
    private final Vector3 topLeft;

    public PixelRenderer(Scene scene, int x, int y, double stepSizeX, double stepSizeY, Vector3 topLeft) {
        this.scene = scene;
        this.x = x;
        this.y = y;
        this.stepSizeX = stepSizeX;
        this.stepSizeY = stepSizeY;
        this.topLeft = topLeft;
    }

    @Override
    public Result call() {
        return new Result(scene.getPixelColor(x, y, stepSizeX, stepSizeY, topLeft), x, y);
    }

    public class Result {
        public Color color;
        public int x;
        public int y;

        public Result(Color color, int x, int y) {
            this.color = color;
            this.x = x;
            this.y = y;
        }
    }
}
