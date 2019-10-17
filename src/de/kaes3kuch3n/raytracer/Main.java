package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.reflect.Type;

public class Main {
    private View view;
    private Camera camera;
    public static void main(String[] args) {
        Camera camera = new Camera(new Vector3(0d,0d,0d), new Vector3(0d,0d, -1.0));
        Sphere sphere = new Sphere(0, 0, -2, 0.5);
        calculatePlane(new View(), camera, sphere);
    }

    public static void calculatePlane(View view, Camera camera, Sphere sphere){
        double stepSizeY = 2.0 / view.getHeight();
        double stepSizeX = 2.0 / view.getWidth();
        BufferedImage image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < view.getHeight(); y++) {
            for (int x = 0; x < view.getWidth(); x++) {
                int pixelPos = x + y * view.getWidth();

                double planePosX = (stepSizeX * x) - 1;
                double planePosY = (stepSizeY * y) - 1;
                double distanceToHit = new Ray(camera.getPosition(), new Vector3(planePosX, planePosY, -1)).getRayHits(sphere);

                int red = 0xFFFF0000;
                int black = 0xFF000000;
                if(distanceToHit > 0)
                    image.setRGB(x, y, red);
                else
                    image.setRGB(x, y, black);
            }
        }
        view.setImage(image);
        view.update(view.getGraphics());
    }
}
