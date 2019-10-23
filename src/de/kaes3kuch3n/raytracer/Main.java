package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        Camera camera = new Camera(new Vector3(0d,0d,0d), new Vector3(0d,0d, -1.0));
        Sphere sphere = new Sphere(0, 0, -2, 0.5);
        Light light = new Light(new Vector3(2, 0, -1), new Color(255, 255, 255), 1f);
        Window window = new Window(800, 800);
        Image image = calculatePlane(window.getSize(), camera, sphere, light);
        window.setImage(new ImagePanel(image));
    }

    public static Image calculatePlane(Dimension imageSize, Camera camera, Sphere sphere, Light light) {
        double stepSizeY = 2.0 / imageSize.height;
        double stepSizeX = 2.0 / imageSize.width;
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < imageSize.height; y++) {
            for (int x = 0; x < imageSize.width; x++) {
                double planePosX = (stepSizeX * x) - 1;
                double planePosY = (stepSizeY * y) - 1;
                Ray.Hit rayHit = new Ray(camera.getPosition(), new Vector3(planePosX, planePosY, -1)).getDistanceFromOrigin(sphere);
                if (rayHit == null) {
                    image.setRGB(x, y, 0xFF000000);
                    continue;
                }

                //Light with normal vector
                Vector3 lightDirection = Vector3.subtract(light.getPosition(), rayHit.position).normalize();
                Vector3 normalVector = Vector3.subtract(rayHit.position, sphere.getPosition()).normalize();
                double lightCos = Vector3.dot(lightDirection, normalVector);
                if (lightCos < 0)
                    lightCos = 0;

                int globalLight = 5;
                int r = Math.min((int) (lightCos * light.getColor().getRed() * light.getIntensity()) + globalLight, 255);
                int g = Math.min((int) (lightCos * light.getColor().getGreen() * light.getIntensity()) + globalLight, 255);
                int b = Math.min((int) (lightCos * light.getColor().getBlue() * light.getIntensity()) + globalLight, 255);

                Color newColor = new Color(r, g, b);

                image.setRGB(x, y, newColor.getRGB());
            }
        }
        return image;
    }
}
