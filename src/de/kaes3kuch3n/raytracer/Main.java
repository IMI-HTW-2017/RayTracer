package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.objects.Sphere;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Camera camera = new Camera(new Vector3(0d,0d,0d), new Vector3(0d,0d, -1.0));
        Sphere sphere = new Sphere(-1, 0, -2, 0.5);
        Sphere sphere2 = new Sphere(1, 0, -2, 0.5);
        Sphere sphere3 = new Sphere(0, 1, -2, 0.5);
        Sphere sphere4 = new Sphere(0, -1, -2, 0.5);
        Light light = new Light(new Vector3(0, 0, -1.5), new Color(255, 4, 0), 1f);
        Window window = new Window(800, 800);

        Scene scene = new Scene(camera, window.getSize());
        scene.addSpheres(sphere, sphere2, sphere3, sphere4);
        scene.addLights(light);
        Image image = scene.renderImage();

        window.setImage(new ImagePanel(image));
    }
}
