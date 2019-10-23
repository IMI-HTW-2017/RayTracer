package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        Camera camera = new Camera(new Vector3(0d,0d,0d), new Vector3(0d,0d, -1.0));
        Sphere sphere = new Sphere(0, 0, -2, 0.5);
        Window window = new Window();
        Image image = calculatePlane(window.getSize(), camera, sphere);
        window.setImage(new ImagePanel(image));
    }

    public static Image calculatePlane(Dimension imageSize, Camera camera, Sphere sphere) {
        double stepSizeY = 2.0 / imageSize.height;
        double stepSizeX = 2.0 / imageSize.width;
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < imageSize.height; y++) {
            for (int x = 0; x < imageSize.width; x++) {
                double planePosX = (stepSizeX * x) - 1;
                double planePosY = (stepSizeY * y) - 1;
                double distanceToHit = new Ray(camera.getPosition(), new Vector3(planePosX, planePosY, -1)).getDistanceFromOrigin(sphere);

                int color = 255 - (int)((distanceToHit - 1.5) * 512);
                int newColor = 0xFF000000 | (color << 16) | (color << 8) | color;
                int black = 0xFF000000;
                if(distanceToHit > 0)
                    image.setRGB(x, y, newColor);
                else
                    image.setRGB(x, y, black);
            }
        }
        return image;
    }
}
