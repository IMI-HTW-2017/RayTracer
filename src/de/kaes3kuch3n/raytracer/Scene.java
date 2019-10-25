package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.objects.Sphere;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scene {
    private List<Sphere> spheres = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    private Camera camera;
    private Dimension imageSize;

    public Scene(Camera camera, Dimension imageSize) {
        this.camera = camera;
        this.imageSize = imageSize;
    }

    public void addSpheres(Sphere... spheres) {
        this.spheres.addAll(Arrays.asList(spheres));
    }

    public void addLights(Light... lights) {
        this.lights.addAll(Arrays.asList(lights));
    }

    public Image renderImage() {
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setPaint(Color.BLACK);
        graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());
        double stepSizeX = 2.0 / imageSize.width;
        double stepSizeY = 2.0 / imageSize.height;
        for (int y = 0; y < imageSize.height; y++) {
            for (int x = 0; x < imageSize.width; x++) {
                double planePosX = (stepSizeX * x) - 1;
                double planePosY = (stepSizeY * y) - 1;
                for (Sphere sphere : spheres) {
                    Ray.Hit rayHit = new Ray(camera.getPosition(), new Vector3(planePosX, planePosY, -1)).getDistanceFromOrigin(sphere);
                    //Nothing hit? -> Black
                    if (rayHit == null) {
                        continue;
                    }
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    for (Light light : lights) {
                        //Light with normal vector
                        Vector3 lightDirection = Vector3.subtract(light.getPosition(), rayHit.position).normalize();
                        Vector3 normalVector = Vector3.subtract(rayHit.position, sphere.getPosition()).normalize();
                        double lightCos = Vector3.dot(lightDirection, normalVector);
                        if (lightCos < 0)
                            lightCos = 0;

                        //Some global lighting
                        int globalLight = 5;
                        r += Math.min((int) (lightCos * light.getColor().getRed() * light.getIntensity()) + globalLight, 255);
                        g += Math.min((int) (lightCos * light.getColor().getGreen() * light.getIntensity()) + globalLight, 255);
                        b += Math.min((int) (lightCos * light.getColor().getBlue() * light.getIntensity()) + globalLight, 255);
                    }
                    r /= lights.size();
                    g /= lights.size();
                    b /= lights.size();
                    int color = new Color(r, g, b).getRGB();
                    image.setRGB(x, y, color);
                }
            }
        }
        return image;
    }

}
