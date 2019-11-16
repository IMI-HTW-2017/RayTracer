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
        Camera camera = new Camera(new Vector3(0d, 0d, 4d), new Vector3(0d, 0d, 3d), 0);
        Window window = new Window(800, 800);
        scene = new Scene(camera);

        scene.addQuadrics(
                new Sphere(1, new Color(255, 0, 0)).scale(1.2, 0.5, 1).translate(0, -1.5, 0),
                new Sphere(0.8, new Color(0, 255, 0)).translate(0, 1.2, 0).rotateZ(65),
                new Sphere(0.8, new Color(0, 255, 0)).translate(0, 1.2, 0).rotateZ(-65)
        );

        scene.addLights(
                new Light(new Vector3(0, 0, 3), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(3, 0, 3), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(0, 0, 15), new Color(255, 255, 255), 1f)
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
