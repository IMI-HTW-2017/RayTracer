package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.*;
import de.kaes3kuch3n.raytracer.utilities.CSGOperator;
import de.kaes3kuch3n.raytracer.utilities.Consts;
import de.kaes3kuch3n.raytracer.utilities.Material;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import javax.swing.*;
import java.awt.*;

public class Main {
    private Scene scene;

    private void show() {
        Camera camera = new Camera(new Vector3(0, 0, 5), new Vector3(0, 0, 4), 0);
        Window window = new Window(800, 800);
        scene = new Scene(camera);

        Material red = Material.CreateRough(new Color(255, 0, 0), 0.01, 0);
        Material green = Material.CreateRough(new Color(0, 255, 0), 0.01, 0);
        Material blue = Material.CreateMetal(new Color(52, 157, 184), 0.3, 0.9);
        Material white = Material.CreateMetal(new Color(255, 255, 255), 0.9, 0.5);
        Material transparent = Material.CreateTransparent(new Color(231, 255, 233), 0.1, 0.01, 0.99, Consts.Refraction.AIR);

        Quadric sphere1 = new Sphere(0.2, transparent).translate(0, 0, 1);
        Quadric sphere2 = new Sphere(1, red).translate(0, 0, -2);
        Quadric sphere3 = new Sphere(2, green).translate(2, 0, -2);
        Quadric cyl = new Cylinder(1, 0, 1, 0.5, transparent).translate(0, 0, 0);

        CSG csg1 = new CSG(sphere2, sphere3, CSGOperator.COMBINE);

        scene.addCSGs(new CSG(sphere2), new CSG(sphere3), new CSG(cyl));

        scene.addLights(
                new Light(new Vector3(0, 0, 4), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(3, 0, 1), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(0, 0, 15), new Color(255, 255, 255), 1f)
        );

        ImagePanel imagePanel = getRenderedImage(window.getSize());
        window.addResizeListener(size -> imagePanel.updateImage(scene.renderImage(size)));
        window.setImage(imagePanel);

        window.addSlider("RotateY", 0, 180, 0, e -> {
            Object eventSource = e.getSource();
            if (!(eventSource instanceof JSlider)) return;

            JSlider slider = (JSlider) eventSource;
            double value = slider.getValue() / 5d;
            sphere3.rotateY(value);
            sphere2.rotateY(value);


            imagePanel.updateImage(scene.renderImage(window.getSize()));
        });
    }

    private ImagePanel getRenderedImage(Dimension size) {
        return new ImagePanel(scene.renderImage(size));
    }

    public static void main(String[] args) {
        new Main().show();
    }
}
