package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.objects.Sphere;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Camera camera = new Camera(new Vector3(0d, 0d, 0d), new Vector3(0d, 0d, -1), 0);
        Window window = new Window(1000, 1000);
        Scene scene = new Scene(camera, window.getSize());

        scene.addSpheres(
                new Sphere(0, 0, -20, 10, new Color(255, 0, 0)),
                new Sphere(0, 0, -2, 0.5, new Color(0, 255, 39))
                //new Sphere(0, -1, -2, 0.5, new Color(255, 0, 0)),
                //new Sphere(0, 1, -2, 0.5, new Color(50, 255, 50))
        );

        scene.addLights(
                new Light(new Vector3(0, 0, -0.5), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(3, 1, -2), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(0, -1, -1), new Color(4, 0, 255), 5)
        );

        Image image = scene.renderImage();
        window.setImage(new ImagePanel(image));
    }
}
