package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.objects.Quadric;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;

public class Main {
    private Scene scene;

    private void show() {
        Camera camera = new Camera(new Vector3(0d, 0d, 4d), new Vector3(0d, 0d, 3d), 0);
        Window window = new Window(1000, 1000);
        scene = new Scene(camera);

        /*
        scene.addSpheres(
                new Sphere(-3, 3, -30, 22, new Color(255, 0, 0)),
                new Sphere(0, 0, -2, 0.5, new Color(0, 255, 39)),
                new Sphere(0.6, 0.1, -1.6, 0.05, new Color(0, 180, 255))
        );

         */
        Quadric quadric = new Quadric(1, 1, 1, 0, 0, 0, 0, 0, 0, -1.1f, new Color(255, 58, 63));
        //quadric.move(0,0,-10);
        scene.addQuadrics(quadric);

        scene.addLights(
                //new Light(new Vector3(6, 0, 0), new Color(255, 255, 255), 1f),
                new Light(new Vector3(0, 0, 10), new Color(255, 255, 255), 1f)
        );

        ImagePanel imagePanel = getRenderedImage(window.getSize());
        window.addResizeListener(size -> imagePanel.updateImage(scene.renderImage(size)));
        window.setImage(imagePanel);
    }

    private ImagePanel getRenderedImage(Dimension size) {
        return new ImagePanel(scene.renderImage(size));
    }

    public static void main(String[] args) {
        new Main().show();
    }
}
