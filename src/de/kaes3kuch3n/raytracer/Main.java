package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.objects.Sphere;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

public class Main {
    private Scene scene;

    private void show() {
        Camera camera = new Camera(new Vector3(0d, 0d, 0d), new Vector3(0d, 0d, -1), 0);
        Window window = new Window(600, 400);
        scene = new Scene(camera);

        scene.addSpheres(
                new Sphere(-3, 3, -30, 22, new Color(255, 0, 0)),
                new Sphere(0, 0, -2, 0.5, new Color(0, 255, 39)),
                new Sphere(0.6, 0.1, -1.6, 0.05, new Color(0, 180, 255))
        );

        scene.addLights(
                new Light(new Vector3(2, 0, -1.2), new Color(255, 255, 255), 1f),
                new Light(new Vector3(0, 0, 0), new Color(255, 255, 255), 0.05f)
        );

        ImagePanel imagePanel = getRenderedImage(window.getSize());
        imagePanel.addResizeListener(size -> imagePanel.updateImage(scene.renderImage(size)));
        window.setImage(imagePanel);
    }

    private ImagePanel getRenderedImage(Dimension size) {
        return new ImagePanel(scene.renderImage(size));
    }

    public static void main(String[] args) {
        new Main().show();
    }
}
